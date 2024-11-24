package com.example.nrfaboekhoudapplicatie.service.dalInterfaces;

import com.example.nrfaboekhoudapplicatie.dal.entity.User;

import java.util.Optional;
import java.util.List;

public interface IUserDAL {
    User saveUser(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAllUsers();
    void deleteUser(Long id);
}