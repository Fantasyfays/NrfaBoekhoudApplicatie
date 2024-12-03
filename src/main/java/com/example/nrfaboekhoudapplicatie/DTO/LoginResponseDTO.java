package com.example.nrfaboekhoudapplicatie.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO<T> {
    private String token;
    private T details;
}
