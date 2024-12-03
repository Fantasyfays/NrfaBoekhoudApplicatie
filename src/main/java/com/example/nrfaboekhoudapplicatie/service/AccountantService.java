package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.DTO.*;
import com.example.nrfaboekhoudapplicatie.dal.implementatie.UserDAL;
import com.example.nrfaboekhoudapplicatie.exceptions.UsernameAlreadyExistsException;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IAccountantDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountantService {

    private final IAccountantDAL accountantDAL;
    private final UserDAL userDAL;

    @Autowired
    public AccountantService(IAccountantDAL accountantDAL, UserDAL userDAL) {
        this.accountantDAL = accountantDAL;
        this.userDAL = userDAL;
    }

    public AccountantReadDTO createAccountant(AccountantCreateDTO createDTO) {
        if (userDAL.existsByUsername(createDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        return accountantDAL.create(createDTO);
    }

    public Optional<AccountantReadDTO> getAccountantById(Long id) {
        return accountantDAL.read(id);
    }
    public Optional<AccountantReadDTO> getAccountantByUserId(Long userId) {
        System.out.println("Fetching accountant details for user ID: " + userId);
        return accountantDAL.readByUserId(userId);
    }


    public AccountantReadDTO updateAccountant(AccountantUpdateDTO updateDTO) {
        if (userDAL.existsByUsername(updateDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        return accountantDAL.update(updateDTO);
    }

    public void deleteAccountant(AccountantDeleteDTO deleteDTO) {
        accountantDAL.delete(deleteDTO);
    }
}
