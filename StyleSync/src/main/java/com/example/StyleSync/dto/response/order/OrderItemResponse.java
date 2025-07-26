package com.example.StyleSync.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Integer id;
    private Integer productId;
    private Integer quantityId;
    private String productName;
    private Double price;
}
