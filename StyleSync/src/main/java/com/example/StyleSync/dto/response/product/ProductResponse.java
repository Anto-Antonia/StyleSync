package com.example.StyleSync.dto.response.product;

import com.example.StyleSync.entity.StockStatus;
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
}
