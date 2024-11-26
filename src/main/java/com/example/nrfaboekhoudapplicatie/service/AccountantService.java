package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.dal.DTO.AccountantCreateDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.AccountantResponseDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.AccountantUpdateDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.dal.repository.UserRepository;
import com.example.nrfaboekhoudapplicatie.enums.RoleType;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IAccountantDAL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AccountantService {

    private final IAccountantDAL accountantDAL;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountantService(IAccountantDAL accountantDAL, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.accountantDAL = accountantDAL;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountantResponseDTO createAccountant(AccountantCreateDTO accountantCreateDTO) {
        if (userRepository.existsByUsername(accountantCreateDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        Accountant accountant = new Accountant();
        accountant.setUsername(accountantCreateDTO.getUsername());
        accountant.setPassword(passwordEncoder.encode(accountantCreateDTO.getPassword()));
        accountant.setCompanyName(accountantCreateDTO.getCompanyName());
        accountant.setEmail(accountantCreateDTO.getEmail());
        accountant.setPhoneNumber(accountantCreateDTO.getPhoneNumber());

        User user = new User();
        user.setUsername(accountant.getUsername());
        user.setPassword(accountant.getPassword());
        user.setRoles(new HashSet<>(Collections.singleton(RoleType.ACCOUNTANT)));

        userRepository.save(user);
        Accountant savedAccountant = accountantDAL.save(accountant);

        return new AccountantResponseDTO(
                savedAccountant.getId(),
                savedAccountant.getUsername(),
                savedAccountant.getCompanyName(),
                savedAccountant.getEmail(),
                savedAccountant.getPhoneNumber()
        );
    }

    public AccountantResponseDTO getAccountantById(long id) {
        Accountant accountant = accountantDAL.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        return new AccountantResponseDTO(
                accountant.getId(),
                accountant.getUsername(),
                accountant.getCompanyName(),
                accountant.getEmail(),
                accountant.getPhoneNumber()
        );
    }

    public List<AccountantResponseDTO> getAllAccountants() {
        return accountantDAL.findAll().stream()
                .map(accountant -> new AccountantResponseDTO(
                        accountant.getId(),
                        accountant.getUsername(),
                        accountant.getCompanyName(),
                        accountant.getEmail(),
                        accountant.getPhoneNumber()
                ))
                .collect(Collectors.toList());
    }

    public AccountantResponseDTO updateAccountant(Long id, AccountantUpdateDTO accountantUpdateDTO) {
        Accountant accountant = accountantDAL.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Accountant not found."));

        if (accountantUpdateDTO.getCompanyName() != null) {
            accountant.setCompanyName(accountantUpdateDTO.getCompanyName());
        }
        if (accountantUpdateDTO.getEmail() != null) {
            accountant.setEmail(accountantUpdateDTO.getEmail());
        }
        if (accountantUpdateDTO.getPhoneNumber() != null) {
            accountant.setPhoneNumber(accountantUpdateDTO.getPhoneNumber());
        }
        if (accountantUpdateDTO.getPassword() != null) {
            accountant.setPassword(passwordEncoder.encode(accountantUpdateDTO.getPassword()));

            User user = userRepository.findByUsername(accountant.getUsername())
                    .orElseThrow(() -> new IllegalStateException("Linked user not found."));
            user.setPassword(accountant.getPassword());
            userRepository.save(user);
        }
        Accountant updatedAccountant = accountantDAL.save(accountant);

        return new AccountantResponseDTO(
                updatedAccountant.getId(),
                updatedAccountant.getUsername(),
                updatedAccountant.getCompanyName(),
                updatedAccountant.getEmail(),
                updatedAccountant.getPhoneNumber()
        );
    }

    public void deleteAccountant(long id) {
        Accountant accountant = accountantDAL.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Accountant not found."));

        userRepository.findByUsername(accountant.getUsername())
                .ifPresent(userRepository::delete);

        accountantDAL.deleteById(id);
    }
}


