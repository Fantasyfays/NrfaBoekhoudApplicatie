package com.example.nrfaboekhoudapplicatie.presentation;

import com.example.nrfaboekhoudapplicatie.DTO.User.CreateUserDTO;
import com.example.nrfaboekhoudapplicatie.DTO.User.UserDTO;
import com.example.nrfaboekhoudapplicatie.DTO.User.UserLoginDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.dal.implementatie.UserNotFoundException;
import com.example.nrfaboekhoudapplicatie.security.JwtTokenProvider;
import com.example.nrfaboekhoudapplicatie.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CreateUserDTO createUserDTO) {
        authService.registerUser(createUserDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            User loggedInUser = authService.loginUser(userLoginDTO);


            String token = jwtTokenProvider.createToken(loggedInUser.getUsername(), loggedInUser.getRole().name());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", loggedInUser.getUsername());
            response.put("role", loggedInUser.getRole().name());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            authService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody CreateUserDTO createUserDTO) {
        try {
            User updatedUser = authService.updateUser(id, createUserDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser() {
        var user = authService.getAuthenticatedUser();
        if (user.isPresent()) {
            System.out.println("User gevonden: " + user.get().getUsername());
            return ResponseEntity.ok(user.get());
        } else {
            System.out.println("Geen gebruiker gevonden.");
            return ResponseEntity.status(401).build();
        }
    }



}
