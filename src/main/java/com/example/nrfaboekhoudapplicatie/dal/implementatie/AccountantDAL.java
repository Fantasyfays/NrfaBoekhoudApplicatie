package com.example.nrfaboekhoudapplicatie.dal.implementatie;

import com.example.nrfaboekhoudapplicatie.DTO.*;
import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import com.example.nrfaboekhoudapplicatie.dal.entity.Role;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.dal.repository.AccountantRepository;
import com.example.nrfaboekhoudapplicatie.dal.repository.UserRepository;
import com.example.nrfaboekhoudapplicatie.service.RoleService;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IAccountantDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountantDAL implements IAccountantDAL {

    private final AccountantRepository accountantRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AccountantDAL(AccountantRepository accountantRepository,
                         UserRepository userRepository,
                         RoleService roleService,
                         BCryptPasswordEncoder passwordEncoder) {
        this.accountantRepository = accountantRepository;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AccountantReadDTO create(AccountantCreateDTO createDTO) {
        if (userRepository.existsByUsername(createDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(createDTO.getPassword());
        Role accountantRole = roleService.getOrCreateRole("ACCOUNTANT");

        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setPassword(hashedPassword);
        user.getRoles().add(accountantRole);

        User savedUser = userRepository.save(user);

        Accountant accountant = new Accountant();
        accountant.setUser(savedUser);

        Accountant savedAccountant = accountantRepository.save(accountant);

        return mapToReadDTO(savedAccountant);
    }

    @Override
    public Optional<AccountantReadDTO> read(Long id) {
        return accountantRepository.findById(id)
                .map(this::mapToReadDTO);
    }

    @Override
    public Optional<AccountantReadDTO> readByUserId(Long userId) {
        return accountantRepository.findByUserId(userId)
                .map(this::mapToReadDTO);
    }

    @Override
    public AccountantReadDTO update(AccountantUpdateDTO updateDTO) {
        Accountant accountant = accountantRepository.findById(updateDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Accountant not found"));

        User user = accountant.getUser();
        user.setUsername(updateDTO.getUsername());

        if (updateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        userRepository.save(user);

        return mapToReadDTO(accountant);
    }

    @Override
    public void delete(AccountantDeleteDTO deleteDTO) {
        Accountant accountant = accountantRepository.findById(deleteDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Accountant not found"));

        accountantRepository.delete(accountant);
    }

    private AccountantReadDTO mapToReadDTO(Accountant accountant) {
        AccountantReadDTO dto = new AccountantReadDTO();
        dto.setId(accountant.getId());
        dto.setUsername(accountant.getUser().getUsername());

        return dto;
    }

}
