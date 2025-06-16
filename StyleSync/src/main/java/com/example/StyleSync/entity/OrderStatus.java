package com.example.StyleSync.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELED
}
