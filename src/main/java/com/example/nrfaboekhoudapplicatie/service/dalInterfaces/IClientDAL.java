package com.example.nrfaboekhoudapplicatie.service.dalInterfaces;

import com.example.nrfaboekhoudapplicatie.dal.entity.Client;

import java.util.List;
import java.util.Optional;

public interface IClientDAL {
    Client save(Client client);
    void deleteById(Long id);
    Optional<Client> findById(Long id);
    List<Client> findAll();
    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Client> findByAccountantId(Long accountantId);
}
