package com.example.nrfaboekhoudapplicatie.presentation;

import com.example.nrfaboekhoudapplicatie.DTO.ClientReadDTO;
import com.example.nrfaboekhoudapplicatie.DTO.LoginResponseDTO;
import com.example.nrfaboekhoudapplicatie.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/client")
public class ClientAuthController {

    private final AuthService authService;

    public ClientAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO<ClientReadDTO>> login(@RequestParam String username, @RequestParam String password) {
        try {
            LoginResponseDTO<ClientReadDTO> response = authService.loginClient(username, password);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(null);
        }
    }
}
