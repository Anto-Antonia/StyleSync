package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.order.OrderRequest;
import com.example.StyleSync.dto.response.order.OrderResponse;
import com.example.StyleSync.entity.*;
import com.example.StyleSync.exceptions.cart.CartIsAlreadyEmpty;
import com.example.StyleSync.exceptions.order.OrderNotFoundException;
import com.example.StyleSync.exceptions.user.UserNotFoundException;
import com.example.StyleSync.mapper.Order_OrderItemMapper;
import com.example.StyleSync.repository.CartRepository;
import com.example.StyleSync.repository.OrderRepository;
import com.example.StyleSync.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartServiceImpl cartService;
    private final Order_OrderItemMapper mapper;
    private final CartRepository cartRepository;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, CartServiceImpl cartService, Order_OrderItemMapper mapper, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.mapper = mapper;
        this.cartRepository = cartRepository;
    }

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest, String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found."));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found for user."));

        List<CartItem> cartItems = cart.getItems();

        if(cartItems.isEmpty()){
            throw new CartIsAlreadyEmpty("Your cart is empty.");
        }

        Order order = mapper.fromOrderRequest(orderRequest, user, cartItems);

        orderRepository.save(order);

        cartService.clearCart(email);
        return mapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getUserOrders(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found."));

        List<Order> orders = user.getOrders();

        return orders.stream().map(mapper::toOrderResponse).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found."));

        List<Order> orders = user.getOrders();

        return orders.stream().map(mapper::toOrderResponse).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Optional<Order> orders = orderRepository.findById(orderId);

        if(orders.isPresent()){
            Order order = orders.get();

            return mapper.toOrderResponse(order);
        } else{
            throw new OrderNotFoundException("Order with id " + orderId + " not found");
        }
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order not found"));

        order.setStatus(newStatus);
        orderRepository.save(order);

        return mapper.toOrderResponse(order);
    }
}
