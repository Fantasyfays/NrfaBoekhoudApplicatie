package com.example.nrfaboekhoudapplicatie.service.interfaces;

import com.example.nrfaboekhoudapplicatie.DTO.*;

import java.util.Optional;

public interface IAccountantDAL {
    AccountantReadDTO create(AccountantCreateDTO createDTO);

    Optional<AccountantReadDTO> read(Long id);

    Optional<AccountantReadDTO> readByUserId(Long userId); // Toegevoegd

    AccountantReadDTO update(AccountantUpdateDTO updateDTO);

    void delete(AccountantDeleteDTO deleteDTO);
}

