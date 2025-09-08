package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.cart.CartResponse;

public interface CartService {
    void addItemToCart(String email, Integer productId, int quantity);
    void removeProductFromCart(String email, Integer productId);
    void updateProductQuantity(String email, Integer productId, int newQuantity);
    CartResponse getUserCart(String email);
    void clearCart(String email);
}
