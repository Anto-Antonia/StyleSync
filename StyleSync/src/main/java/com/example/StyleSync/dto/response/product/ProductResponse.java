package com.example.StyleSync.dto.response.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer id;
    private String productName;
    private Double price;
    private Integer quantity;
    private String stockStatus;

    public ProductResponse(String productName, Double price) {
        this.productName = productName;
        this.price = price;
    }
}
