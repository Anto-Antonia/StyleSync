package com.example.StyleSync.dto.request.order;

import com.example.StyleSync.entity.PaymentMethod;
import com.example.StyleSync.entity.ShippingAddress;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    private List<OrderItemRequest> items;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private ShippingAddress shippingAddress;
}
