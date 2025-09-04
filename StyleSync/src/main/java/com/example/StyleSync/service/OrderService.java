package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.order.OrderRequest;
import com.example.StyleSync.dto.response.order.OrderResponse;
import com.example.StyleSync.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest, String email);
    List<OrderResponse> getUserOrders(Integer userId);
    List<OrderResponse> getMyOrders(String email);
    OrderResponse getOrderById(Long orderId);
    OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);
}
