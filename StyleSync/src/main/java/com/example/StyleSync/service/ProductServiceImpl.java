package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.product.ProductRequest;
import com.example.StyleSync.dto.request.product.UpdateProductRequest;
import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.exceptions.product.ProductNotFoundException;
import com.example.StyleSync.mapper.ProductMapper;
import com.example.StyleSync.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductMapper mapper, ProductRepository productRepository) {
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(ProductRequest productRequest) {
        Product product = mapper.fromProductRequest(productRequest);
        return productRepository.save(product);
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + "'" + id + "'" + " not found"));

        return mapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> product = productRepository.findAll();
        List<ProductResponse> responses = product.stream()
                .map(mapper::toProductResponse).collect(Collectors.toList());
        return responses;
    }

    @Override
    public void updateProduct(Integer id, UpdateProductRequest updateProductRequest) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isPresent()){
            Product product1 = optionalProduct.get();
            product1.setProductName(updateProductRequest.getProductName());
            product1.setPrice(updateProductRequest.getPrice());
            product1.setQuantity(updateProductRequest.getQuantity());

            productRepository.save(product1);
        }
        else{
            throw new ProductNotFoundException("Product with ID " + id + " not found.");
        }

    }

    @Override
    public void removeProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
