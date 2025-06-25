package com.example.StyleSync.dto.response.order;

import com.example.StyleSync.entity.OrderStatus;
import com.example.StyleSync.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Integer id;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String shippingAddress;

    private List<OrderItemResponse> items;
    private Double totalMount;

    private Integer userId;
    private String userName;
}
