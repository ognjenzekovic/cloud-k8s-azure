package com.cloud.invoice.client;

import com.cloud.invoice.dto.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderClient {

    private final RestTemplate restTemplate;
    private final String orderBaseUrl;

    public OrderClient(RestTemplate restTemplate,
                       @Value("${order.service.url}") String orderBaseUrl) {
        this.restTemplate = restTemplate;
        this.orderBaseUrl = orderBaseUrl;
    }

    public OrderDto getOrder(Long orderId) {
        return restTemplate.getForObject(
                orderBaseUrl + "/api/orders/" + orderId,
                OrderDto.class
        );
    }

    public void updateStatus(Long orderId, String status) {
        restTemplate.put(
                orderBaseUrl + "/api/orders/" + orderId + "/status?status=" + status,
                null
        );
    }

    public void updateInvoiceUrl(Long orderId, String invoiceUrl) {
        restTemplate.put(
                orderBaseUrl + "/api/orders/" + orderId + "/invoice-url?invoiceUrl=" + invoiceUrl,
                null
        );
    }
}