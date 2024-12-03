package com.example.nrfaboekhoudapplicatie.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClientReadDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Long accountantId;
}
