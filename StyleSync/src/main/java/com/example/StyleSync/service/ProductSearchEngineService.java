package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.product.ProductResponse;

import java.util.List;

public interface ProductSearchEngineService {
    List<ProductResponse> searchProductByKeyword(String keyword);
    List<ProductResponse> searchProductByName(String productName);
}
