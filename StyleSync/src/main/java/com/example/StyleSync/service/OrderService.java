package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.order.OrderRequest;
import com.example.StyleSync.dto.response.order.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest, Integer userId);
    List<OrderResponse> getUserOrders(Integer userId);
    OrderResponse getOrderById(Long orderId);
}
