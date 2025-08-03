package com.example.StyleSync.service;

import com.example.StyleSync.dto.response.order.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(Integer userId, Integer shippingAddressId);
    List<OrderResponse> getUserOrders(Integer userId);
    OrderResponse getOrderById(Integer orderId);
}
