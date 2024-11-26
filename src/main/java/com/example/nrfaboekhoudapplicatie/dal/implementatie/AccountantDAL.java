package com.example.nrfaboekhoudapplicatie.dal.implementatie;

import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import com.example.nrfaboekhoudapplicatie.dal.repository.AccountantRepository;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IAccountantDAL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AccountantDAL implements IAccountantDAL {

    private final AccountantRepository accountantRepository;

    public AccountantDAL(AccountantRepository accountantRepository) {
        this.accountantRepository = accountantRepository;
    }

    @Override
    public Accountant save(Accountant accountant) {
        return accountantRepository.save(accountant);
    }

    @Override
    public void deleteById(Long id) {
        accountantRepository.deleteById(id);
    }

    @Override
    public Optional<Accountant> findById(Long id) {
        return accountantRepository.findById(id);
    }

    @Override
    public List<Accountant> findAll() {
        return accountantRepository.findAll();
    }

    @Override
    public Optional<Accountant> findByUsername(String username) {
        return accountantRepository.findByUsername(username);
    }

    @Override
    public Optional<Accountant> findByEmail(String email) {
        return accountantRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return accountantRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountantRepository.existsByEmail(email);
    }
}
