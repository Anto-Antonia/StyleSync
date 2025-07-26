//package com.example.StyleSync.exceptions;
//
//import com.example.StyleSync.exceptions.cart.CartItemNotFoundException;
//import com.example.StyleSync.exceptions.cart.CartNotFoundException;
//import com.example.StyleSync.exceptions.cart.InvalidCartOperationException;
//import com.example.StyleSync.exceptions.error.ApiError;
//import com.example.StyleSync.exceptions.order.InvalidOrderStatusException;
//import com.example.StyleSync.exceptions.order.OrderCreationException;
//import com.example.StyleSync.exceptions.order.OrderNotFoundException;
//import com.example.StyleSync.exceptions.product.OutOfStockException;
//import com.example.StyleSync.exceptions.product.ProductNotFoundException;
//import com.example.StyleSync.exceptions.role.RoleNotFoundException;
//import com.example.StyleSync.exceptions.user.EmailAlreadyExistsException;
//import com.example.StyleSync.exceptions.user.UserNotFoundException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.time.LocalDateTime;
//
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
//        return new ResponseEntity<>(apiError, apiError.getStatus());
//    }
//
//    @ExceptionHandler(ProductNotFoundException.class)
//    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_FOUND, ex, request));
//    }
//
//    @ExceptionHandler(OutOfStockException.class)
//    public ResponseEntity<Object> handleOutOfStock(OutOfStockException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_FOUND, ex, request));
//    }
//
//    @ExceptionHandler(RoleNotFoundException.class)
//    public ResponseEntity<Object> handleRoleNotFound(RoleNotFoundException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_FOUND, ex, request));
//    }
//
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_FOUND, ex, request));
//    }
//
//    @ExceptionHandler(EmailAlreadyExistsException.class)
//    public ResponseEntity<Object> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.BAD_REQUEST, ex, request));
//    }
//
//    @ExceptionHandler(OrderNotFoundException.class)
//    public ResponseEntity<Object> handleOrderNotFound(OrderNotFoundException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_FOUND, ex, request));
//    }
//
//    @ExceptionHandler(OrderCreationException.class)
//    public ResponseEntity<Object> handleOrderCreation(OrderCreationException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.BAD_REQUEST, ex, request));
//    }
//
//    @ExceptionHandler(InvalidOrderStatusException.class)
//    public ResponseEntity<Object> handleInvalidOrderStatus(InvalidOrderStatusException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_ACCEPTABLE, ex, request));
//    }
//
//    @ExceptionHandler(CartItemNotFoundException.class)
//    public ResponseEntity<Object> handleCartItemNotFound(CartItemNotFoundException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_FOUND, ex, request));
//    }
//
//    @ExceptionHandler(CartNotFoundException.class)
//    public ResponseEntity<Object> handleCartNotFound(CartNotFoundException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_FOUND, ex, request));
//    }
//
//    @ExceptionHandler(InvalidCartOperationException.class)
//    public ResponseEntity<Object> handleInvalidCartOperation(InvalidCartOperationException ex, HttpServletRequest request) {
//        return buildResponseEntity(buildApiError(HttpStatus.NOT_ACCEPTABLE, ex, request));
//    }
//
//    private ApiError buildApiError(HttpStatus status, Exception ex, HttpServletRequest request) {
//        return ApiError.builder()
//                .timeStamp(LocalDateTime.now())
//                .status(status)
//                .message(ex.getMessage())
//                .debugMessage(ExceptionUtils.getRootCauseMessage(ex))
//                .path(request.getRequestURI())
//                .build();
//    }
//}
