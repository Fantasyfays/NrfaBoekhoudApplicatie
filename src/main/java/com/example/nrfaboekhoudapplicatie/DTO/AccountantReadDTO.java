package com.example.nrfaboekhoudapplicatie.DTO;

import lombok.Data;

@Data
public class AccountantReadDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
