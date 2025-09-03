package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.order.OrderRequest;
import com.example.StyleSync.dto.response.order.OrderResponse;
import com.example.StyleSync.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest, String email);
    List<OrderResponse> getUserOrders(Integer userId);
    OrderResponse getOrderById(Long orderId);
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
}
