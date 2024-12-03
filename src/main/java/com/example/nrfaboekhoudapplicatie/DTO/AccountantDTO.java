package com.example.nrfaboekhoudapplicatie.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class AccountantDTO {

    // User-specific fields
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
