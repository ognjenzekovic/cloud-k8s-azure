package com.cloud.catalog.controller;

import com.cloud.catalog.dto.StockUpdateRequest;
import com.cloud.catalog.model.Product;
import com.cloud.catalog.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/reduce-stock")
    public ResponseEntity<String> reduceStock(@RequestBody StockUpdateRequest request) {
        try {
            productService.reduceStock(request.getItems());
            return ResponseEntity.ok("Stock updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}