package com.cloud.order.service;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class QueueServiceImpl implements QueueService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.queue-name}")
    private String queueName;

    private QueueClient queueClient;
    private final ObjectMapper objectMapper;

    public QueueServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        queueClient = new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(queueName)
                .buildClient();
        queueClient.createIfNotExists();
    }

    @Override
    public void sendInvoiceMessage(Long orderId) {
        try {
            String json = objectMapper.writeValueAsString(Map.of("orderId", orderId));
            String encoded = Base64.getEncoder().encodeToString(json.getBytes());
            queueClient.sendMessage(encoded);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send queue message", e);
        }
    }
}