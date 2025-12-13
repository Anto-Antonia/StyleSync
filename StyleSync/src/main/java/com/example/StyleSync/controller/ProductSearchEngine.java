package com.example.StyleSync.controller;

import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.service.ProductSearchEngineServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search/")
public class ProductSearchEngine {

    private final ProductSearchEngineServiceImpl service;

    public ProductSearchEngine(ProductSearchEngineServiceImpl service) {
        this.service = service;
    }

    @GetMapping("keyword")
    public ResponseEntity<List<ProductResponse>> searchProductByKeyword (@RequestParam String keyword){
        List<ProductResponse> responses = service.searchProductByKeyword(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("name")
    public ResponseEntity<List<ProductResponse>> searchProductByName(@RequestParam String productName){
        List<ProductResponse> responses = service.searchProductByName(productName);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
