package com.example.nrfaboekhoudapplicatie.DTO.User;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
