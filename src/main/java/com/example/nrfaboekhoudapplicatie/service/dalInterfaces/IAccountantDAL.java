package com.example.nrfaboekhoudapplicatie.service.dalInterfaces;

import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;

import java.util.List;
import java.util.Optional;

public interface IAccountantDAL {
    Accountant save(Accountant accountant);
    void deleteById(Long id);
    Optional<Accountant> findById(Long id);
    List<Accountant> findAll();
    Optional<Accountant> findByUsername(String username);
    Optional<Accountant> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
