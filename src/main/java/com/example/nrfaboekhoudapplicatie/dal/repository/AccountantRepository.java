package com.example.nrfaboekhoudapplicatie.dal.repository;

import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountantRepository extends JpaRepository<Accountant, Long> {

    Optional<Accountant> findByUserId(Long userId);
}
