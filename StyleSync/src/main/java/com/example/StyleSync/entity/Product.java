package com.example.StyleSync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String productName;
    private Double price;
    private int quantity;

    @ManyToMany(mappedBy = "favoriteProducts")
    private List<User> favoriteByUser = new ArrayList<>();
    @Transient // makes sure this property is NOT PERSISTED TO THE DB
    private StockStatus getStockStatus(){
        if(quantity == 0){
            return StockStatus.OUT_OF_STOCK;
        } else if(quantity <= 10){
            return StockStatus.LIMITED_STOCK;
        } else {
            return StockStatus.IN_STOCK;
        }
    }
}
