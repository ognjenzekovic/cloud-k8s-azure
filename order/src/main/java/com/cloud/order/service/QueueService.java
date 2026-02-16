package com.cloud.order.service;

public interface QueueService {
    void sendInvoiceMessage(Long orderId);
}
