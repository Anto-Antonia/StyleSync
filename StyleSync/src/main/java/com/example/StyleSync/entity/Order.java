package com.example.StyleSync.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Integer id;

    private LocalDateTime orderDate;

    @Column(columnDefinition = "VARCHAR(20)") // prevents Hibernate from mapping this field to MySQL native enum type
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> itemList = new ArrayList<>();

    @Column(columnDefinition = "VARCHAR(20)") // prevents Hibernate from mapping this field to MySQL native enum type
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Embedded()
    private ShippingAddress shippingAddress;
}
