package com.cloud.order.service;

import com.cloud.order.client.CatalogClient;
import com.cloud.order.dto.CreateOrderRequest;
import com.cloud.order.dto.OrderItemRequest;
import com.cloud.order.dto.ProductDto;
import com.cloud.order.model.Order;
import com.cloud.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CatalogClient catalogClient;

    @Mock
    private QueueService queueService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCreateOrder_EmptyItems_ThrowsException() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("CUST-1");
        request.setCustomerName("Test User");
        request.setItems(List.of());

        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(request);
        });

        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_Success() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("CUST-1");
        request.setCustomerName("Test User");

        OrderItemRequest itemReq = new OrderItemRequest();
        itemReq.setProductId(1L);
        itemReq.setQuantity(2);
        request.setItems(List.of(itemReq));

        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(new BigDecimal("999.99"));

        when(catalogClient.getProduct(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });

        Order result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals("CUST-1", result.getCustomerId());
        assertEquals(new BigDecimal("1999.98"), result.getTotalPrice());
        verify(catalogClient).reduceStock(any());
        verify(queueService).sendInvoiceMessage(1L);
    }
}