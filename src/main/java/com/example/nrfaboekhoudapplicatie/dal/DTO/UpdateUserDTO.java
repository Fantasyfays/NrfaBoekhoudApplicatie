package com.example.nrfaboekhoudapplicatie.dal.DTO;

import com.example.nrfaboekhoudapplicatie.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private String username;
    private String password;
    private Set<RoleType> roles;
}
