//package com.example.StyleSync.mapper;
//
//import com.example.StyleSync.dto.request.shipping.ShippingAddressRequest;
//import com.example.StyleSync.dto.response.shipping.ShippingAddressResponse;
//import com.example.StyleSync.entity.ShippingAddress;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ShippingAddressMapper {
//    public ShippingAddress fromShippingAddressRequest(ShippingAddressRequest request){
//        ShippingAddress address = new ShippingAddress();
//        address.setFullName(request.getFullName());
//        address.setPhoneNumber(request.getPhoneNumber());
//        address.setCity(request.getCity());
//        address.setCountry(request.getCountry());
//        address.setAddress(request.getAddress());
//        address.setPostalCode(request.getPostalCode());
//
//        return address;
//    }
//
//    public ShippingAddressResponse toShippingAddressResponse(ShippingAddress address){
//        ShippingAddressResponse response = new ShippingAddressResponse();
//        response.setFullName(address.getFullName());
//        response.setPhoneNumber(address.getPhoneNumber());
//        response.setCity(address.getCity());
//        response.setCountry(address.getCountry());
//        response.setAddress(address.getAddress());
//        response.setPostalCode(address.getPostalCode());
//
//        return response;
//    }
//}
