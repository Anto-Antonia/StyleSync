package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.product.ProductRequest;
import com.example.StyleSync.dto.request.product.UpdateProductRequest;
import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(ProductRequest productRequest);
    ProductResponse getProductById(Integer id);
    List<ProductResponse> getAllProducts();
    void updateProduct(Integer id, UpdateProductRequest updateProductRequest);
    void removeProduct(Integer id);
}
