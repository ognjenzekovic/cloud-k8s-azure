package com.cloud.order.client;

import com.cloud.order.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Component
public class CatalogClient {

    private final RestTemplate restTemplate;
    private final String catalogBaseUrl;

    public CatalogClient(RestTemplate restTemplate,
                         @Value("${catalog.service.url}") String catalogBaseUrl) {
        this.restTemplate = restTemplate;
        this.catalogBaseUrl = catalogBaseUrl;
    }

    public ProductDto getProduct(Long productId) {
        return restTemplate.getForObject(
                catalogBaseUrl + "/api/products/" + productId,
                ProductDto.class
        );
    }

    public void reduceStock(Map<Long, Integer> items) {
        Map<String, Object> body = Map.of("items", items);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                catalogBaseUrl + "/api/products/reduce-stock",
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to reduce stock: " + response.getBody());
        }
    }
}