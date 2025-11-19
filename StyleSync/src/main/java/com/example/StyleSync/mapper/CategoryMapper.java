package com.example.StyleSync.mapper;

import com.example.StyleSync.dto.request.category.CategoryRequest;
import com.example.StyleSync.dto.response.category.CategoryResponse;
import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {
    public Category fromCategoryRequest(CategoryRequest request){
        Category category = new Category();

        category.setCategoryName(request.getCategoryName());
        category.setProductList(new ArrayList<>());

        return category;
    }

    public CategoryResponse toCategoryResponse(Category category){
        CategoryResponse response = new CategoryResponse();
        response.setCategoryName(category.getCategoryName());

        List<ProductResponse> productResponseList = category.getProductList()
                .stream()
                .map(product -> new ProductResponse(product.getProductName(), product.getPrice())).toList();

        response.setProducts(productResponseList);
        return response;
    }
}
