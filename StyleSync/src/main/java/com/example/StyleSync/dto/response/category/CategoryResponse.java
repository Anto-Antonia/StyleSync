package com.example.StyleSync.dto.response.category;

import com.example.StyleSync.dto.response.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String categoryName;
    private List<ProductResponse> products;
}
