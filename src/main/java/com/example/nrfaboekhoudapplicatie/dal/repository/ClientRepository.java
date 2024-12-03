package com.example.nrfaboekhoudapplicatie.dal.repository;

import com.example.nrfaboekhoudapplicatie.dal.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUserId(Long userId); // Deze query wordt automatisch gegenereerd door Spring Data JPA
}
