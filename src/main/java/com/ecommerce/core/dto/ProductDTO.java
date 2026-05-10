package com.ecommerce.core.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    private List<String> images;
    
    @Min(value = 0, message = "Stock must be positive")
    private Integer stockQuantity;
}
