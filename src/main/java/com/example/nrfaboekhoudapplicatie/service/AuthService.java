package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.DTO.User.CreateUserDTO;
import com.example.nrfaboekhoudapplicatie.DTO.User.UserDTO;
import com.example.nrfaboekhoudapplicatie.DTO.User.UserLoginDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.dal.implementatie.UserNotFoundException;
import com.example.nrfaboekhoudapplicatie.security.JwtTokenProvider;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IUserDAL;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final IUserDAL userDAL;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    public AuthService(IUserDAL userDAL, BCryptPasswordEncoder passwordEncoder) {
        this.userDAL = userDAL;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(CreateUserDTO createUserDTO) {
        if (userDAL.findByUsername(createUserDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = User.builder()
                .username(createUserDTO.getUsername())
                .email(createUserDTO.getEmail())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .role(createUserDTO.getRole())
                .build();

        return userDAL.save(user);
    }

    public User updateUser(Long id, CreateUserDTO createUserDTO) {
        User user = userDAL.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        if (createUserDTO.getPassword() != null && !createUserDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        }
        user.setRole(createUserDTO.getRole());

        return userDAL.save(user);
    }


    public User loginUser(UserLoginDTO userLoginDTO) {
        User user = userDAL.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return user;
    }

    public Optional<User> getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            return userDAL.findByUsername(username);
        }

        return Optional.empty();
    }


    public void deleteUser(Long id) throws UserNotFoundException {
        if (!userDAL.existsById(id)) {
            throw new UserNotFoundException("User does not exist");
        }
        userDAL.deleteById(id);
    }

    public List<UserDTO> getAllUsers() {
        return userDAL.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name()))
                .collect(Collectors.toList());
    }
}
