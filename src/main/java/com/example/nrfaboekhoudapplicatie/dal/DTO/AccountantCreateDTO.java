package com.example.nrfaboekhoudapplicatie.dal.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountantCreateDTO {
    private String username;
    private String password;
    private String companyName;
    private String email;
    private String phoneNumber;
}
