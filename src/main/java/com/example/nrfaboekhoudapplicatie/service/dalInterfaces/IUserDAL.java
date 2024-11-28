package com.example.nrfaboekhoudapplicatie.service.dalInterfaces;

import com.example.nrfaboekhoudapplicatie.dal.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAL {

    User saveUser(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndRole(String username, String role); // Zorg ervoor dat deze signature correct is.

    List<User> findAllUsers();

    void deleteUser(Long id);
}
