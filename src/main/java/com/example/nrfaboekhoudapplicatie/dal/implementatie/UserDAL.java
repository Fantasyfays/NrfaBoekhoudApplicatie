package com.example.nrfaboekhoudapplicatie.dal.implementatie;

import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.dal.repository.UserRepository;
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
        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
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
