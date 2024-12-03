package com.example.nrfaboekhoudapplicatie.service.interfaces;

import com.example.nrfaboekhoudapplicatie.dal.entity.User;

import java.util.Optional;

public interface IUserDAL {

    Optional<User> findByUsernameAndRole(String username, String role);

    Optional<User> findClientByUsername(String username);

    Optional<User> findAccountantByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    void deleteById(Long id);
}
