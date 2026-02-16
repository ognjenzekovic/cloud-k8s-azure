package com.cloud.order.controller;

import com.cloud.order.dto.CreateOrderRequest;
import com.cloud.order.model.Order;
import com.cloud.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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