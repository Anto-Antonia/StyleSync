package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.cart.CartResponse;

public interface CartService {
    void addItemToCart(Integer userId, Integer productId, int quantity);
    void removeProductFromCart(Integer userId, Integer productId);
    void updateProductQuantity(Integer userId, Integer productId, int newQuantity);
    CartResponse getUserCat(Integer userId);
    void clearCart(Integer userId);
}
