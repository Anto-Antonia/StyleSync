package com.example.StyleSync.service;

import com.example.StyleSync.entity.Cart;
import com.example.StyleSync.entity.CartItem;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.entity.User;
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
        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setQuantity(2);
        existingItem.setCart(cart);

        cart.getItems().add(existingItem);

        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        service.addItemToCart(EMAIL, PRODUCT_ID, 1);

        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());

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
        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setQuantity(1);
        existingItem.setCart(cart);

        cart.getItems().add(existingItem);

        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        service.removeProductFromCart(EMAIL, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());

        verify(userRepository, times(1)).findUserByEmail(EMAIL);
        verify(cartItemRepository, times(1)).delete(existingItem);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void removeProductFromCart_whenProductNotInCart_throwError(){
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(CartItemNotFoundException.class, ()-> service.removeProductFromCart(EMAIL, PRODUCT_ID));

        verify(cartItemRepository, never()).delete(any());
        verify(cartRepository, never()).save(any());

    }
}
