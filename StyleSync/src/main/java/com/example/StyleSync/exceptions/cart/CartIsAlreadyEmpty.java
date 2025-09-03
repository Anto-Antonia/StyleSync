package com.example.StyleSync.exceptions.cart;

public class CartIsAlreadyEmpty extends RuntimeException{
    public CartIsAlreadyEmpty(String message) {
        super(message);
    }
}
