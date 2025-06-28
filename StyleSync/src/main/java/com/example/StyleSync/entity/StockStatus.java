package com.example.StyleSync.entity;

import lombok.Getter;

@Getter
public enum StockStatus {
    IN_STOCK("In stock"),
    LIMITED_STOCK("Limited Stock"),
    OUT_OF_STOCK("Out of Stock");

    private final String label;

    StockStatus(String label) {
        this.label = label;
    }
}
