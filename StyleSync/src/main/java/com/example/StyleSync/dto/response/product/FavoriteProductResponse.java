package com.example.StyleSync.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteProductResponse {
    private String productName;
    private double price;
    private int quantity;
}
