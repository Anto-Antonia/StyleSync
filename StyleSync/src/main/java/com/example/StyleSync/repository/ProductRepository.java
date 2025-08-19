package com.example.StyleSync.repository;

import com.example.StyleSync.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // For partial search
    List<Product> findByProductNameContainingIgnoreCase(String keyword);

    //for exact match
    List<Product>findByProductNameIgnoreCase(String productName);
}
