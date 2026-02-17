package com.cloud.order.controller;

import com.cloud.order.dto.CreateOrderRequest;
import com.cloud.order.model.Order;
import com.cloud.order.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import java.io.ByteArrayOutputStream;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Value("${azure.storage.connection-string}")
    private String blobConnectionString;

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}/invoice")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);

        if (order.getInvoiceUrl() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            BlobClient blobClient = new BlobClientBuilder()
                    .connectionString(blobConnectionString)
                    .containerName("invoices")
                    .blobName("invoice-order-" + id + ".pdf")
                    .buildClient();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.downloadStream(outputStream);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "inline; filename=invoice-order-" + id + ".pdf")
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(request);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
                                               @RequestParam Order.Status status) {
        orderService.updateStatus(id, status);
        return ResponseEntity.ok("Status updated");
    }

    @PutMapping("/{id}/invoice-url")
    public ResponseEntity<String> updateInvoiceUrl(@PathVariable Long id,
                                                   @RequestParam String invoiceUrl) {
        orderService.updateInvoiceUrl(id, invoiceUrl);
        return ResponseEntity.ok("Invoice URL updated");
    }
}