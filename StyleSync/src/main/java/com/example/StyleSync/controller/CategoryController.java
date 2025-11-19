package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.category.CategoryRequest;
import com.example.StyleSync.entity.Category;
import com.example.StyleSync.service.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryServiceImpl service;

    public CategoryController(CategoryServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryRequest request){
        Category category = service.addCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
}
