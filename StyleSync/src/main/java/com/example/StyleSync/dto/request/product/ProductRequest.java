package com.example.StyleSync.dto.request.product;

import com.example.StyleSync.entity.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank // used only on string
    private String productName;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    @Min(0)
    private int quantity;

    private String categoryName;
}
