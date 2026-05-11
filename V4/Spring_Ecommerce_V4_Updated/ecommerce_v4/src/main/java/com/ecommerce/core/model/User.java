package com.ecommerce.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username is required")
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "password is required")
    @Column(nullable = false)
    private String password;

    private String role; // ADMIN, CUSTOMER

    @com.ecommerce.core.validation.CameroonPhone
    private String phoneNumber;
}
