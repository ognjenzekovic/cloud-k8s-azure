package com.cloud.invoice.service;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.models.QueueMessageItem;
import com.cloud.invoice.client.OrderClient;
import com.cloud.invoice.dto.OrderDto;
import com.cloud.invoice.model.InvoiceMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class QueueListenerServiceImpl implements QueueListenerService {
    private static final Logger log = LoggerFactory.getLogger(QueueListenerService.class);

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.queue-name}")
    private String queueName;

    private QueueClient queueClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderClient orderClient;
    private final InvoiceGeneratorService invoiceGenerator;
    private final BlobStorageService blobStorage;

    public QueueListenerServiceImpl(OrderClient orderClient,
                                InvoiceGeneratorService invoiceGenerator,
                                BlobStorageService blobStorage) {
        this.orderClient = orderClient;
        this.invoiceGenerator = invoiceGenerator;
        this.blobStorage = blobStorage;
    }

    @PostConstruct
    public void init() {
        queueClient = new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(queueName)
                .buildClient();
    }

    @Override
    @Scheduled(fixedDelay = 5000)
    public void pollQueue() {
        QueueMessageItem message = null;
        try {
            var messages = queueClient.receiveMessages(1);
            for (QueueMessageItem msg : messages) {
                message = msg;
                processMessage(msg);
                queueClient.deleteMessage(msg.getMessageId(), msg.getPopReceipt());
            }
        } catch (Exception e) {
            log.error("Error processing queue message", e);
        }
    }

    private void processMessage(QueueMessageItem message) throws Exception {
        String decoded = new String(Base64.getDecoder().decode(message.getBody().toString()));
        InvoiceMessage invoiceMessage = objectMapper.readValue(decoded, InvoiceMessage.class);
        Long orderId = invoiceMessage.getOrderId();

        log.info("Processing invoice for order: {}", orderId);

        orderClient.updateStatus(orderId, "PROCESSING");

        OrderDto order = orderClient.getOrder(orderId);

        byte[] pdfBytes = invoiceGenerator.generateInvoice(order);

        String fileName = "invoice-order-" + orderId + ".pdf";
        String blobUrl = blobStorage.uploadPdf(pdfBytes, fileName);

        orderClient.updateInvoiceUrl(orderId, blobUrl);

        log.info("Invoice generated and uploaded for order: {}. URL: {}", orderId, blobUrl);
    }
}
