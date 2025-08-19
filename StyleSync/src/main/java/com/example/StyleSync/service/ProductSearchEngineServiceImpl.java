package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.exceptions.product.ProductNotFoundException;
import com.example.StyleSync.mapper.ProductMapper;
import com.example.StyleSync.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchEngineServiceImpl implements ProductSearchEngineService{
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductSearchEngineServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductResponse> searchProductByKeyword(String keyword) {
        List<Product> responses = repository.findByProductNameContainingIgnoreCase(keyword);

        if(responses.isEmpty()){
            throw new ProductNotFoundException("Product with name '" + keyword + "' was not found");
        }

        return responses.stream().map(mapper::toProductResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchProductByName(String productName) {
        List<Product> responses = repository.findByProductNameIgnoreCase(productName);

        if(responses.isEmpty()){
            throw new ProductNotFoundException("The product you tried searching for was not found :(");
        }
        return responses.stream().map(mapper::toProductResponse).collect(Collectors.toList());
    }
}
