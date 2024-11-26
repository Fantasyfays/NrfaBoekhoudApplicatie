package com.example.nrfaboekhoudapplicatie.dal.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
