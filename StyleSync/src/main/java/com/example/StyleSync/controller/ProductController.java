package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.product.ProductRequest;
import com.example.StyleSync.dto.request.product.UpdateProductRequest;
import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.service.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody ProductRequest productRequest){
        Product product = service.addProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable @Valid Integer id){
        ProductResponse response = service.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProduct(){
        List<ProductResponse> responses = service.getAllProducts();
        return ResponseEntity.ok().body(responses);
    }

    @PatchMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id, @RequestBody UpdateProductRequest updateProductRequest){
        ProductResponse response = service.updateProduct(id, updateProductRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeProduct(@PathVariable @Valid Integer id){
        service.removeProduct(id);
        return ResponseEntity.ok("The product with id " + id + " has been removed");
    }

    @PutMapping("/{productId}/category/{categoryName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> addCategoryToProduct(@PathVariable Integer productId, @PathVariable String categoryName){
        ProductResponse response = service.addCategoryToProduct(productId, categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
