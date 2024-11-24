package com.example.nrfaboekhoudapplicatie.dal.DTO;

import com.example.nrfaboekhoudapplicatie.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private Set<RoleType> roles;
    private String token; // Optioneel: gebruik dit bij JWT
}
