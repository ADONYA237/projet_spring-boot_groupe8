package com.ecommerce.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateShippingRequest {

    @NotBlank(message = "status is required")
    @Pattern(regexp = "PENDING|PAID|SHIPPED|DELIVERED", message = "status must be one of: PENDING, PAID, SHIPPED, DELIVERED")
    private String status;
}
