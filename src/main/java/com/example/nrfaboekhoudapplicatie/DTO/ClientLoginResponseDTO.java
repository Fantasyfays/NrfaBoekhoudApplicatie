package com.example.nrfaboekhoudapplicatie.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientLoginResponseDTO {
    private String token;
    private ClientReadDTO client;
}
