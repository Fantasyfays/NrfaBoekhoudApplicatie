package com.example.nrfaboekhoudapplicatie.DTO;

import lombok.Data;

@Data
public class ClientCreateDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Long accountantId;
}
