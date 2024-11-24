package com.example.nrfaboekhoudapplicatie.dal.DTO;

import com.example.nrfaboekhoudapplicatie.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private RoleType name;
}
