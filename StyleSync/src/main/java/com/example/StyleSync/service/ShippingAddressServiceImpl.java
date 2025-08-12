package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.shipping.ShippingAddressRequest;
import com.example.StyleSync.dto.response.shipping.ShippingAddressResponse;
import com.example.StyleSync.entity.*;
import com.example.StyleSync.exceptions.address.AddressNotFoundException;
import com.example.StyleSync.exceptions.order.OrderNotFoundException;
import com.example.StyleSync.exceptions.user.UserNotFoundException;
import com.example.StyleSync.mapper.ShippingAddressMapper;
import com.example.StyleSync.repository.OrderRepository;
import com.example.StyleSync.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ShippingAddressServiceImpl implements ShippingAddressService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ShippingAddressMapper mapper;

    public ShippingAddressServiceImpl(UserRepository userRepository, OrderRepository orderRepository, ShippingAddressMapper mapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public ShippingAddress addAddress(Integer userId, ShippingAddressRequest shippingAddressRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User with id " + userId + " not found."));

        ShippingAddress address = mapper.fromShippingAddressRequest(shippingAddressRequest);

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.PAYPAL);

        orderRepository.save(order);
        return address;
    }

    @Override
    public void updateAddress(Long orderId, ShippingAddressRequest shippingAddressRequest) {
       Order order = orderRepository.findById(orderId)
               .orElseThrow(()-> new OrderNotFoundException("Order not found"));

       ShippingAddress updatedAddress = mapper.fromShippingAddressRequest(shippingAddressRequest);
       order.setShippingAddress(updatedAddress);

       orderRepository.save(order);
    }

    @Override
    public void deleteAddress(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order with id " + orderId + " not found"));

        order.setShippingAddress(null);
        orderRepository.save(order);
    }

    @Override
    public ShippingAddressResponse getUserAddress(Integer userId) {
        List<Order> orderList = orderRepository.findByUserIdOrderByOrderDateDesc(userId);

        if(orderList.isEmpty()){
            throw new AddressNotFoundException("No orders found for user with id " + userId);
        }

        ShippingAddress address = orderList.get(0).getShippingAddress();

        if(address == null){
            throw new AddressNotFoundException("No shipping address found for latest order.");
        }

        return mapper.toShippingAddressResponse(address);
    }
}
