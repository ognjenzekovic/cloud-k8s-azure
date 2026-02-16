package com.cloud.order.dto;

import java.util.List;

public class CreateOrderRequest {

    private String customerId;
    private String customerName;
    private List<OrderItemRequest> items;

    public CreateOrderRequest() {}

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}