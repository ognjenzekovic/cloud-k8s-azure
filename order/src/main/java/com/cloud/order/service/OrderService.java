package com.cloud.order.service;

import com.cloud.order.dto.CreateOrderRequest;
import com.cloud.order.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(CreateOrderRequest request);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    void updateStatus(Long id, Order.Status status);
    void updateInvoiceUrl(Long id, String invoiceUrl);
}
