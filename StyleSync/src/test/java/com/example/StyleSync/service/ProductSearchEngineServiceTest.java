package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.product.ProductResponse;
import com.example.StyleSync.entity.Category;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.mapper.ProductMapper;
import com.example.StyleSync.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductSearchEngineServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductSearchEngineServiceImpl service;

    @Mock
    private ProductMapper mapper;

    private Product product;
    private Category category;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp(){

        category = new Category();
        category.setId(1);
        category.setCategoryName("Tops");

        product = new Product();
        product.setId(1);
        product.setProductName("Beige Shirt");
        product.setPrice(9.99);
        product.setQuantity(30);
        product.setCategory(category);

        productResponse = new ProductResponse();
        productResponse.setId(1);
        productResponse.setProductName("Beige Shirt");
        productResponse.setStockStatus("In stock");
        productResponse.setPrice(9.99);
        productResponse.setQuantity(30);
        productResponse.setCategoryName("Tops");
    }

    @Test
    void searchByKeyword_whenProductFound_ReturnResponseList(){
        when(productRepository.findByProductNameContainingIgnoreCase("Shirt")).thenReturn(List.of(product));
        when(mapper.toProductResponse(product)).thenReturn(productResponse);

        List<ProductResponse> responses = service.searchProductByKeyword("Shirt");

        assertEquals(1, responses.size());
        assertEquals("Beige Shirt", responses.get(0).getProductName());
        assertEquals("Tops", responses.get(0).getCategoryName());

        verify(productRepository, times(1)).findByProductNameContainingIgnoreCase("Shirt");
        verify(mapper, times(1)).toProductResponse(product);
    }

    @Test
    void searchByKeyword_whenProductNotFound_throwsProductNotFoundError(){

    }
}
