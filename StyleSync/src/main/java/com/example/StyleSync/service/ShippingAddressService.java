package com.example.StyleSync.service;

import com.example.StyleSync.dto.request.shipping.ShippingAddressRequest;
import com.example.StyleSync.dto.response.shipping.ShippingAddressResponse;
import com.example.StyleSync.entity.ShippingAddress;

public interface ShippingAddressService {
    ShippingAddress addAddress(Integer userId, ShippingAddressRequest shippingAddressRequest);
    void updateAddress(Long orderId, ShippingAddressRequest shippingAddressRequest);
    void deleteAddress(Long orderId);
    ShippingAddressResponse getUserAddress(Integer userId);
}
