package com.cloud.invoice.service;

import com.cloud.invoice.dto.OrderDto;

public interface InvoiceGeneratorService {
    byte[] generateInvoice(OrderDto order);
}
