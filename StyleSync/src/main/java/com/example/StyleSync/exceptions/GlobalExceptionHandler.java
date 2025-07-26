package com.example.StyleSync.exceptions;

import com.example.StyleSync.exceptions.cart.CartItemNotFoundException;
import com.example.StyleSync.exceptions.cart.CartNotFoundException;
import com.example.StyleSync.exceptions.cart.InvalidCartOperationException;
import com.example.StyleSync.exceptions.error.ApiError;
import com.example.StyleSync.exceptions.order.InvalidOrderStatusException;
import com.example.StyleSync.exceptions.order.OrderCreationException;
import com.example.StyleSync.exceptions.order.OrderNotFoundException;
import com.example.StyleSync.exceptions.product.OutOfStockException;
import com.example.StyleSync.exceptions.product.ProductNotFoundException;
import com.example.StyleSync.exceptions.role.RoleNotFoundException;
import com.example.StyleSync.exceptions.user.EmailAlreadyExistsException;
import com.example.StyleSync.exceptions.user.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<Object> handleOutOfStockException(OutOfStockException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.IM_USED)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }
    @ExceptionHandler(OrderCreationException.class)
    public ResponseEntity<Object> handleOrderCreationException(OrderCreationException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<Object> handleInvalidOrderStatusException(InvalidOrderStatusException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Object> handleCartItemNotFoundException(CartItemNotFoundException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Object> handleCartNotFoundException(CartNotFoundException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }

    @ExceptionHandler(InvalidCartOperationException.class)
    public ResponseEntity<Object> handleInvalidCartOperationException(InvalidCartOperationException exception, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .message(exception.getMessage())
                .debugMessage((ExceptionUtils.getRootCauseMessage(exception)))
                .path(request.getRequestURI())
                .build();

        return buildResponseEntity(error);
    }
}
