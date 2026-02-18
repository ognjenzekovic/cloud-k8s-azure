package com.cloud.catalog.service;

import com.cloud.catalog.model.Product;
import com.cloud.catalog.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testReduceStock_Success() {
        Product product = new Product("TEST-001", "Test Product", null, new BigDecimal("100.00"), 10);
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.reduceStock(Map.of(1L, 3));

        assertEquals(7, product.getStockQuantity());
        verify(productRepository).save(product);
    }

    @Test
    void testReduceStock_InsufficientStock() {
        Product product = new Product("TEST-001", "Test Product", null, new BigDecimal("100.00"), 2);
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () -> {
            productService.reduceStock(Map.of(1L, 5));
        });

        verify(productRepository, never()).save(any());
    }
}