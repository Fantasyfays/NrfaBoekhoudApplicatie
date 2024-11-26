package com.example.nrfaboekhoudapplicatie.dal.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountantResponseDTO {
    private Long id;
    private String username;
    private String companyName;
    private String email;
    private String phoneNumber;
}
