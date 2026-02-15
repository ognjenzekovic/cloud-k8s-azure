package com.cloud.catalog.service;

import com.cloud.catalog.model.Product;
import com.cloud.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Override
    @Transactional
    public void reduceStock(Map<Long, Integer> items) {
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            Long productId = entry.getKey();
            int requestedQty = entry.getValue();

            Product product = getProductById(productId);

            if (product.getStockQuantity() < requestedQty) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName()
                                + ". Available: " + product.getStockQuantity()
                                + ", Requested: " + requestedQty
                );
            }

            product.setStockQuantity(product.getStockQuantity() - requestedQty);
            productRepository.save(product);
        }
    }
}
