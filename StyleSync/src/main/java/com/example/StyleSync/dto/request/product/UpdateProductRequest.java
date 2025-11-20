package com.example.StyleSync.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    private String productName;
    private Integer quantity;
    private Double price;
    private String categoryName;
}
