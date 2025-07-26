package com.example.StyleSync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    private String fullName;
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private Integer postalCode;
}
