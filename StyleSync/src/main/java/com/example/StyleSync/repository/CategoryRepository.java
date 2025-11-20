package com.example.StyleSync.repository;

import com.example.StyleSync.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCategoryName(String categoryName);
    Optional<Category> findByCategoryName(String categoryName);

}
