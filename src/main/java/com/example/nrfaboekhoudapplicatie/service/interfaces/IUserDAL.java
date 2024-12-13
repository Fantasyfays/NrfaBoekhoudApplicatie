package com.example.nrfaboekhoudapplicatie.service.interfaces;

import com.example.nrfaboekhoudapplicatie.dal.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAL {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    void deleteById(Long id);
    boolean existsById(Long id);
    List<User> findAll();
}