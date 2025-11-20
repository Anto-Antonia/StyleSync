package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.category.CategoryRequest;
import com.example.StyleSync.dto.response.category.CategoryResponse;
import com.example.StyleSync.entity.Category;
import com.example.StyleSync.service.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        List<CategoryResponse> responseList = service.getAllCategories();

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id){
        CategoryResponse response = service.getCategoryById(id);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeCategory(@PathVariable Integer id){
        service.removeCategory(id);

        return ResponseEntity.ok("The category with id " + id + " has been removed.");
    }
}
