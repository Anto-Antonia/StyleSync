package com.example.StyleSync.exceptions.cart;

public class InvalidCartOperationException extends RuntimeException{
    public InvalidCartOperationException(String message) {
        super(message);
    }
}
