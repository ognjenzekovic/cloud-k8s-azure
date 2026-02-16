package com.cloud.invoice.model;

public class InvoiceMessage {

    private Long orderId;

    public InvoiceMessage() {}

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}