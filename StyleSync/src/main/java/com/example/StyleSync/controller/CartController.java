package com.example.StyleSync.controller;

import com.example.StyleSync.dto.response.cart.CartResponse;
import com.example.StyleSync.service.CartServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartServiceImpl service;

    public CartController(CartServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> addItemToCart(@PathVariable Integer productId, @RequestParam int quantity, Authentication authentication){
        String email = authentication.getName();
        service.addItemToCart(email, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/removeItem/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Integer productId, Authentication authentication){
        String email = authentication.getName();
        service.removeProductFromCart(email, productId);
        return ResponseEntity.noContent().build(); // will return 204 No Content if successful
    }

    @PatchMapping("/{productId}/")
    public ResponseEntity<Void> updateProductQuantity(@PathVariable Integer productId, @RequestParam int newQuantity, Authentication authentication){
        String email = authentication.getName();
        service.updateProductQuantity(email, productId, newQuantity);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartResponse> getUserCart(Authentication authentication){
        String email = authentication.getName();
        CartResponse response = service.getUserCart(email);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication){
        String email = authentication.getName();
        service.clearCart(email);

        return ResponseEntity.noContent().build();
    }
}
