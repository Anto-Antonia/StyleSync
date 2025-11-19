package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.category.CategoryRequest;
import com.example.StyleSync.dto.response.category.CategoryResponse;
import com.example.StyleSync.entity.Category;
import com.example.StyleSync.mapper.CategoryMapper;
import com.example.StyleSync.repository.CategoryRepository;
import com.example.StyleSync.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper, ProductRepository productRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    @Override
    public Category addCategory(CategoryRequest request) {
        if(repository.existsByCategoryName(request.getCategoryName())){
            throw new IllegalArgumentException("Category already exists: " + request.getCategoryName());
        }

        Category category = mapper.fromCategoryRequest(request);
        return repository.save(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> responses = repository.findAll();

        return responses.stream()
                .map(mapper::toCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Integer id) {
        Category category = repository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Category not found with id " + id ));

        return mapper.toCategoryResponse(category);
    }

    @Override
    public void removeCategory(Integer id){
        repository.deleteById(id);
    }
}
