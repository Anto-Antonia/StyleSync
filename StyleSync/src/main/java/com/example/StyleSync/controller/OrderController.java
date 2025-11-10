package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.order.OrderRequest;
import com.example.StyleSync.dto.request.order.UpdateOrderStatusRequest;
import com.example.StyleSync.dto.response.order.OrderResponse;
import com.example.StyleSync.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderServiceImpl service;

    public OrderController(OrderServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/place_Order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request, Authentication authentication){
        String email = authentication.getName();
        OrderResponse response = service.placeOrder(request, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/order/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getUserOrders (@PathVariable Integer userId){
        List<OrderResponse> responses = service.getUserOrders(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication){
        String email = authentication.getName();
        List<OrderResponse> responses = service.getMyOrders(email);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId){
        OrderResponse response = service.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse>updateOrderStatus(@PathVariable Long orderId, @Valid @RequestBody UpdateOrderStatusRequest newStatus){
        OrderResponse response = service.updateOrderStatus(orderId, newStatus.getStatus());

        return ResponseEntity.ok(response);
    }
}
