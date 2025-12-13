package com.example.StyleSync.mapper;

import com.example.StyleSync.dto.request.product.ProductRequest;
import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Category;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Product fromProductRequest(ProductRequest productRequest){
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());

        Category category = categoryRepository.findByCategoryName(productRequest.getCategoryName())
                        .orElseThrow(()-> new IllegalArgumentException("Category not found: " + productRequest.getCategoryName()));
        product.setCategory(category);

        return product;
    }

    public ProductResponse toProductResponse(Product product){
        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.getId());
        productResponse.setProductName(product.getProductName());
        productResponse.setPrice(product.getPrice());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setStockStatus(product.getStockStatus().getLabel()); // calling the label from enum
        productResponse.setCategoryName(product.getCategory().getCategoryName());

        return productResponse;
    }
}
