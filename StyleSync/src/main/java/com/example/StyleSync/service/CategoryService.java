package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.category.CategoryRequest;
import com.example.StyleSync.dto.response.category.CategoryResponse;
import com.example.StyleSync.entity.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Integer id);
    void removeCategory(Integer id);
}
