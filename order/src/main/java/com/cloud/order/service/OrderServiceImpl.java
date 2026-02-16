package com.cloud.order.service;

import com.cloud.order.client.CatalogClient;
import com.cloud.order.dto.CreateOrderRequest;
import com.cloud.order.dto.OrderItemRequest;
import com.cloud.order.dto.ProductDto;
import com.cloud.order.model.Order;
import com.cloud.order.model.OrderItem;
import com.cloud.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CatalogClient catalogClient;
    private final QueueService queueService;

    public OrderServiceImpl(OrderRepository orderRepository,
                        CatalogClient catalogClient,
                        QueueService queueService) {
        this.orderRepository = orderRepository;
        this.catalogClient = catalogClient;
        this.queueService = queueService;
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setCustomerName(request.getCustomerName());

        BigDecimal totalPrice = BigDecimal.ZERO;
        Map<Long, Integer> stockReduction = new HashMap<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            if (itemReq.getQuantity() == null || itemReq.getQuantity() < 1) {
                throw new RuntimeException("Quantity must be at least 1");
            }

            ProductDto product = catalogClient.getProduct(itemReq.getProductId());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            order.addItem(orderItem);

            totalPrice = totalPrice.add(
                    product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()))
            );

            stockReduction.put(product.getId(), itemReq.getQuantity());
        }

        catalogClient.reduceStock(stockReduction);

        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        queueService.sendInvoiceMessage(savedOrder.getId());

        return savedOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Order.Status status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateInvoiceUrl(Long id, String invoiceUrl) {
        Order order = getOrderById(id);
        order.setInvoiceUrl(invoiceUrl);
        order.setStatus(Order.Status.COMPLETED);
        orderRepository.save(order);
    }
}