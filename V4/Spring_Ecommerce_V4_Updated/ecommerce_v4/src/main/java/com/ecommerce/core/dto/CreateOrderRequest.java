package com.ecommerce.core.dto;

import com.ecommerce.core.model.OrderItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotEmpty(message = "items must not be empty")
    private List<OrderItem> items;

    private String promoCode;

    @NotBlank(message = "address is required")
    private String address;
}
