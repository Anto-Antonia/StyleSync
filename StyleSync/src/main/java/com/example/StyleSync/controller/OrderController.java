package com.example.StyleSync.controller;

import com.example.StyleSync.dto.request.order.OrderRequest;
import com.example.StyleSync.dto.response.order.OrderResponse;
import com.example.StyleSync.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderServiceImpl service;

    public OrderController(OrderServiceImpl service) {
        this.service = service;
    }

    @PostMapping("{/userId}")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request, @Valid @RequestParam Integer userId){
        OrderResponse response = service.placeOrder(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
