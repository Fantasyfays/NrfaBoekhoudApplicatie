package com.example.nrfaboekhoudapplicatie.presentation;

import com.example.nrfaboekhoudapplicatie.DTO.AccountantReadDTO;
import com.example.nrfaboekhoudapplicatie.DTO.LoginResponseDTO;
import com.example.nrfaboekhoudapplicatie.service.AuthService;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IAccountantDAL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/accountant")
public class AccountantAuthController {

    private final AuthService authService;

    public AccountantAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO<AccountantReadDTO>> login(@RequestParam String username, @RequestParam String password) {
        try {
            LoginResponseDTO<AccountantReadDTO> response = authService.loginAccountant(username, password);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(null);
        }
    }
}
