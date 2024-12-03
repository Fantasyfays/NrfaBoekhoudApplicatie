package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.DTO.ClientReadDTO;
import com.example.nrfaboekhoudapplicatie.DTO.AccountantReadDTO;
import com.example.nrfaboekhoudapplicatie.DTO.LoginResponseDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.security.JwtTokenProvider;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IUserDAL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private final IUserDAL userDAL;
    private final ClientService clientService;
    private final AccountantService accountantService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(IUserDAL userDAL, ClientService clientService, AccountantService accountantService,
                       BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userDAL = userDAL;
        this.clientService = clientService;
        this.accountantService = accountantService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDTO<ClientReadDTO> loginClient(String username, String password) {
        log.debug("Attempting login for username: {}", username);

        Optional<User> userOptional = userDAL.findByUsernameAndRole(username, "CLIENT");
        if (!userOptional.isPresent()) {
            log.error("User not found or role mismatch for username: {}", username);
            throw new IllegalArgumentException("User not found or role mismatch");
        }

        User user = userOptional.get();
        log.debug("Found user: {}", user);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("Invalid password for username: {}", username);
            throw new IllegalArgumentException("Invalid credentials");
        }

        ClientReadDTO clientDetails = clientService.getClientByUserId(user.getId())
                .orElseThrow(() -> {
                    log.error("No client details found for user ID: {}", user.getId());
                    return new IllegalArgumentException("No client details found");
                });

        String token = jwtTokenProvider.createToken(username, "CLIENT", clientDetails);
        log.debug("Generated JWT token: {}", token);

        return new LoginResponseDTO<>(token, clientDetails);
    }

    public LoginResponseDTO<AccountantReadDTO> loginAccountant(String username, String password) {
        log.debug("Attempting login for accountant username: {}", username);

        Optional<User> userOptional = userDAL.findByUsernameAndRole(username, "ACCOUNTANT");
        if (!userOptional.isPresent()) {
            log.error("User not found or role mismatch for username: {}", username);
            throw new IllegalArgumentException("User not found or role mismatch");
        }

        User user = userOptional.get();
        log.debug("Found accountant user: {}", user);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("Invalid password for username: {}", username);
            throw new IllegalArgumentException("Invalid credentials");
        }

        AccountantReadDTO accountantDetails = accountantService.getAccountantByUserId(user.getId())
                .orElseThrow(() -> {
                    log.error("No accountant details found for user ID: {}", user.getId());
                    return new IllegalArgumentException("No accountant details found");
                });

        String token = jwtTokenProvider.createToken(username, "ACCOUNTANT", accountantDetails);
        log.debug("Generated JWT token: {}", token);

        return new LoginResponseDTO<>(token, accountantDetails);
    }
}
