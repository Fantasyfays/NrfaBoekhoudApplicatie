package com.example.nrfaboekhoudapplicatie.dal.implementatie;

import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.dal.repository.UserRepository;
import com.example.nrfaboekhoudapplicatie.enums.RoleType;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IUserDAL;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDAL implements IUserDAL {

    private final UserRepository userRepository;

    public UserDAL(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        System.out.println("Checking for existing username: " + username);
        Optional<User> user = userRepository.findByUsername(username);
        System.out.println("User found: " + user.isPresent());
        return user;
    }

    @Override
    public Optional<User> findByUsernameAndRole(String username, String role) {
        try {
            // Converteer de rol van String naar RoleType
            RoleType roleType = RoleType.valueOf(role.toUpperCase());
            return userRepository.findByUsernameAndRole(username, roleType);
        } catch (IllegalArgumentException e) {
            // Retourneer Optional.empty() als de rol niet geldig is
            return Optional.empty();
        }
    }
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
