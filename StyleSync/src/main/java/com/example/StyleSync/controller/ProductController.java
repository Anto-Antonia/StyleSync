package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.product.ProductRequest;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductRequest productRequest){
        Product product = service.addProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
