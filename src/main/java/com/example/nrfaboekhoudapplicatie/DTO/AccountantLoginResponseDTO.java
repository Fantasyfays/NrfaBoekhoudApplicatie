package com.example.nrfaboekhoudapplicatie.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountantLoginResponseDTO {
    private String token;
    private String username;
    private Long accountantId;
}
