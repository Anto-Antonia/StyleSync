package com.example.StyleSync.mapper;

import com.example.StyleSync.dto.request.product.ProductRequest;
import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product fromProductRequest(ProductRequest productRequest){
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());

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
