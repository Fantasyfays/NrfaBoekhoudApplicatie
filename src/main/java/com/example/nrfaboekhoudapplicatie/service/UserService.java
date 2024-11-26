package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.dal.DTO.CreateUserDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.UpdateUserDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.UserDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IUserDAL;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final IUserDAL userDAL;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(IUserDAL userDAL, BCryptPasswordEncoder passwordEncoder) {
        this.userDAL = userDAL;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userDAL.findAllUsers().stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles()
                ))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userDAL.findById(id)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles()
                ));
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userDAL.findByUsername(username)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles()
                ));
    }

    public UserDTO createUser(CreateUserDTO createUserDTO) {
        if (userDAL.findByUsername(createUserDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        user.setRoles(createUserDTO.getRoles());

        User savedUser = userDAL.saveUser(user);
        return new UserDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRoles()
        );
    }

    public Optional<UserDTO> updateUser(Long id, UpdateUserDTO updateUserDTO) {
        Optional<User> existingUser = userDAL.findById(id);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with the given ID does not exist.");
        }

        User user = existingUser.get();

        if (!user.getUsername().equals(updateUserDTO.getUsername())) {
            if (userDAL.findByUsername(updateUserDTO.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username already exists.");
            }
            user.setUsername(updateUserDTO.getUsername());
        }

        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        if (updateUserDTO.getRoles() != null) {
            user.setRoles(updateUserDTO.getRoles());
        }

        User updatedUser = userDAL.saveUser(user);
        return Optional.of(new UserDTO(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRoles()
        ));
    }

    public boolean deleteUser(Long id) {
        Optional<User> user = userDAL.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with the given ID does not exist.");
        }

        userDAL.deleteUser(id);
        return true;
    }
}
