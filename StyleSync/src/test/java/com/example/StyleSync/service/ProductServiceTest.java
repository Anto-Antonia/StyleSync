package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.product.ProductRequest;
import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Category;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.exceptions.product.ProductNotFoundException;
import com.example.StyleSync.mapper.ProductMapper;
import com.example.StyleSync.repository.CategoryRepository;
import com.example.StyleSync.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMapper mapper;
    @InjectMocks
    private ProductServiceImpl service;

    private Product product;
    private Category category;
    private ProductResponse response;

    @BeforeEach
    void setUp(){

        product = new Product();
        product.setId(1);
        product.setProductName("Hat");
        product.setPrice(9.99);
        product.setQuantity(100);

        category = new Category();
        category.setId(1);
        category.setCategoryName("Head wears");
        category.setProductList(List.of(product));

        product.setCategory(category);

    }

    @Test
    void addProduct_whenSuccessful_returnProduct(){
        ProductRequest request = new ProductRequest("Hat", 9.99, 100, "Head wears");

        when(mapper.fromProductRequest(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = service.addProduct(request);

        assertNotNull(result);
        assertEquals("Hat", result.getProductName());
         verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProductById_whenProductExists_returnProduct(){
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(mapper.toProductResponse(product)).thenReturn(new ProductResponse(product.getId(), product.getProductName(), product.getPrice(), product.getQuantity(), product.getStockStatus().getLabel(), product.getCategory().getCategoryName()));

        ProductResponse response = service.getProductById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Hat", response.getProductName());
        assertEquals(9.99, response.getPrice());
        assertEquals(100, response.getQuantity());
        assertEquals("Head wears", response.getCategoryName());


    }
    @Test
    void getProductById_whenProductNotFound_throwException(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, ()-> service.getProductById(1));

        verify(mapper, never()).toProductResponse(any());
    }
}
