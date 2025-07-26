package com.example.StyleSync.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
