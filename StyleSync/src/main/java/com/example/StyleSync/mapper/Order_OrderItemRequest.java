package com.example.StyleSync.mapper;

import com.example.StyleSync.dto.request.order.OrderRequest;
import com.example.StyleSync.dto.response.order.OrderResponse;
import com.example.StyleSync.entity.Order;
import com.example.StyleSync.entity.OrderItem;
import com.example.StyleSync.entity.OrderStatus;
import com.example.StyleSync.entity.Product;
import com.example.StyleSync.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Order_OrderItemRequest {

    private final ProductRepository productRepository;
    private final ShippingAddressMapper shippingAddressMapper;

    public Order_OrderItemRequest(ProductRepository productRepository, ShippingAddressMapper shippingAddressMapper) {
        this.productRepository = productRepository;
        this.shippingAddressMapper = shippingAddressMapper;
    }

    public Order fromOrderRequest(OrderRequest request){
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddress(shippingAddressMapper.fromShippingAddressRequest(request.getShippingAddress()));

        List<OrderItem> orderItems =request.getItems().stream()
                .map(itemRequest -> {
                    OrderItem orderItem = new OrderItem();
                    Product product =productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(()-> new RuntimeException("Product not found."));

                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setPriceAtPurchase(BigDecimal.valueOf(product.getPrice())); // assuming product.getPrice() returns Double
                    orderItem.setOrder(order);

                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setItemList(orderItems);

        return order;
    }

    public OrderResponse toOrderResponse(Order order){ // gonna finish this method later
        OrderResponse response = new OrderResponse();

        return null;
    }
}
