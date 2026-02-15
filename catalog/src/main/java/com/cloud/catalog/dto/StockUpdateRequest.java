package com.cloud.catalog.dto;

import java.util.Map;

public class StockUpdateRequest {

    private Map<Long, Integer> items;

    public StockUpdateRequest() {}

    public Map<Long, Integer> getItems() { return items; }
    public void setItems(Map<Long, Integer> items) { this.items = items; }
}