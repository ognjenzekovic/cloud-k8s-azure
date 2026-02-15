package com.cloud.catalog.service;

import com.cloud.catalog.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void reduceStock(Map<Long, Integer> items);
}
