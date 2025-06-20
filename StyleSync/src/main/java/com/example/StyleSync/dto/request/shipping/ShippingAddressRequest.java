package com.example.StyleSync.dto.request.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressRequest {
    private String street;
    private String city;
    private String country;
    private Integer postalCode;
}
