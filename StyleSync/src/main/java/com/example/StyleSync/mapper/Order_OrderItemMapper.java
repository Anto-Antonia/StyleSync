package com.example.StyleSync.mapper;

import com.example.StyleSync.dto.request.order.OrderRequest;
import com.example.StyleSync.dto.response.order.OrderItemResponse;
import com.example.StyleSync.dto.response.order.OrderResponse;
import com.example.StyleSync.entity.*;
import com.example.StyleSync.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Order_OrderItemMapper {

    private final ProductRepository productRepository;
    private final ShippingAddressMapper shippingAddressMapper;

    public Order_OrderItemMapper(ProductRepository productRepository, ShippingAddressMapper shippingAddressMapper) {
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

    public OrderResponse toOrderResponse(Order order){
        OrderResponse response = new OrderResponse();

       //response.setId(order.get);
        response.setCreatedAt(order.getOrderDate());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setStatus(order.getStatus());

        ShippingAddress address = new ShippingAddress();
        String addressAsString = String.format(
                "%s, %s, %s, %s, %s, %s",
                address.getFullName(),
                address.getAddress(),
                address.getCity(),
                address.getCountry(),
                address.getPostalCode(),
                address.getPhoneNumber()
        );
            response.setShippingAddress(addressAsString);

            // mapping the order items
            List<OrderItemResponse> responseList = order.getItemList().stream()
                    .map(item ->{
                        OrderItemResponse itemResponse = new OrderItemResponse();
                        itemResponse.setId(item.getId());
                        itemResponse.setProductId(item.getProduct().getId());
                        itemResponse.setProductName(item.getProduct().getProductName());
                        itemResponse.setQuantityId(item.getQuantity());
                        itemResponse.setPrice(item.getPriceAtPurchase().doubleValue());

                        return itemResponse;
            })
                    .collect(Collectors.toList());
            response.setItems(responseList);

            // calculating total
            double total = responseList.stream().mapToDouble(item ->
                    item.getPrice() * item.getQuantityId()).sum();

            response.setTotalMount(total);

            // user info
        response.setUserId(order.getUser().getId());
        response.setUserName(order.getUser().getFirstName() + " " + order.getUser().getLastName());

        return response;
    }
}
