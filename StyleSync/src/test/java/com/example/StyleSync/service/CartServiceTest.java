package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.cart.CartItemResponse;
import com.example.StyleSync.dto.response.cart.CartResponse;
import com.example.StyleSync.entity.Cart;
import com.example.StyleSync.entity.CartItem;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.entity.User;
import com.example.StyleSync.exceptions.cart.CartIsAlreadyEmpty;
import com.example.StyleSync.exceptions.cart.CartItemNotFoundException;
import com.example.StyleSync.exceptions.product.ProductNotFoundException;
import com.example.StyleSync.exceptions.user.UserNotFoundException;
import com.example.StyleSync.mapper.Cart_CartItemMapper;
import com.example.StyleSync.repository.CartItemRepository;
import com.example.StyleSync.repository.CartRepository;
import com.example.StyleSync.repository.ProductRepository;
import com.example.StyleSync.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CartServiceImpl service;

    @Mock
    private Cart_CartItemMapper mapper;

    private Product product;
    private Cart cart;
    private User user;
    private CartItem cartItem;

    private static final String EMAIL = "user@email.com";
    private static final Integer PRODUCT_ID = 1;

    @BeforeEach
    void setup(){
        user = new User();
        user.setId(1);
        user.setEmail(EMAIL);

        product = new Product();
        product.setId(PRODUCT_ID);
        product.setProductName("Shirt");
        product.setPrice(10.89);
        product.setQuantity(30);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);

        cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        user.setCart(cart);

        cartItem.setCart(cart);

    }

    @Test
    void addItemToCart_whenCartIsEmpty_addNewItem(){
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        service.addItemToCart(EMAIL, PRODUCT_ID, 1);

        assertEquals(1, cart.getItems().size());
        assertEquals(1, cart.getItems().get(0).getQuantity());

        verify(cartRepository).save(cart);
    }

    @Test
    void addItemToCart_whenProductAlreadyExists_UpdateQuantity(){

        cart.getItems().add(cartItem);

        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        service.addItemToCart(EMAIL, PRODUCT_ID, 1);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());

        verify(cartRepository).save(cart);
    }

    @Test
    void addItemToCart_whenUserNotFound_throwException(){
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> service.addItemToCart(EMAIL, PRODUCT_ID, 2));

        verify(cartRepository, never()).save(any());

    }

    @Test
    void addItemToCart_whenProductNotFound_throwException(){
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, ()-> service.addItemToCart(EMAIL, PRODUCT_ID, 2));

        verify(cartRepository, never()).save(any());
    }

    @Test
    void removeProductFromCart_whenSuccessful_removeItemFromCart(){

        cart.getItems().add(cartItem);

        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        service.removeProductFromCart(EMAIL, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());

        verify(userRepository, times(1)).findUserByEmail(EMAIL);
        verify(cartItemRepository, times(1)).delete(cartItem);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void removeProductFromCart_whenProductNotInCart_throwError(){
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(CartItemNotFoundException.class, ()-> service.removeProductFromCart(EMAIL, PRODUCT_ID));

        verify(cartItemRepository, never()).delete(any());
        verify(cartRepository, never()).save(any());

    }

    @Test
    void updateProductQuantity_whenSuccessful_updateQuantity(){
        cart.getItems().add(cartItem);

        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        service.updateProductQuantity(EMAIL, PRODUCT_ID, 3);

        assertEquals(3, cartItem.getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void updateProductQuantity_whenUserNotFound_throwException(){
       when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

       assertThrows(UserNotFoundException.class, ()-> service.updateProductQuantity(EMAIL, PRODUCT_ID, 3));

       verify(userRepository).findUserByEmail(EMAIL);
       verify(cartRepository, never()).save(any());
    }

    @Test
    void updateProductQuantity_whenProductNotFound_throwException(){
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(ProductNotFoundException.class, ()-> service.updateProductQuantity(EMAIL, PRODUCT_ID, 3));

        verify(cartRepository, never()).save(any());
    }

    @Test
    void updateProductQuantity_whenQuantityIsZero_removeProduct(){
        cart.getItems().add(cartItem);

        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        service.updateProductQuantity(EMAIL, PRODUCT_ID, 0);

        assertTrue(cart.getItems().isEmpty());

        verify(cartItemRepository).delete(cartItem);
        verify(cartRepository).save(any());
    }

    @Test
    void getUserCart_whenSuccessful_returnUserCart(){
        cart.getItems().add(cartItem);

        CartItemResponse itemResponse = new CartItemResponse();
        itemResponse.setId(1);
        itemResponse.setProductId(1);
        itemResponse.setProductName("Hat");
        itemResponse.setPrice(9.99);
        itemResponse.setQuantity(1);
        itemResponse.setTotalPrice(9.99);

        CartResponse response = new CartResponse();
        response.setId(1);
        response.setItems(List.of(itemResponse));
        response.setTotal(9.99);

        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(mapper.toCartResponse(cart)).thenReturn(response);

        CartResponse result = service.getUserCart(EMAIL);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(9.99, result.getTotal());

        verify(userRepository).findUserByEmail(EMAIL);
        verify(mapper).toCartResponse(cart);

    }

    @Test
    void getUserCart_whenUserNotFound_throwError(){
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> service.getUserCart(EMAIL));

        verify(mapper, never()).toCartResponse(any());
    }

    @Test
    void getUserCart_whenCartIsEmpty_throwEmptyCartError(){
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
        assertThrows(CartIsAlreadyEmpty.class, ()-> service.getUserCart(EMAIL));

        verify(mapper, never()).toCartResponse(any());
    }
}
