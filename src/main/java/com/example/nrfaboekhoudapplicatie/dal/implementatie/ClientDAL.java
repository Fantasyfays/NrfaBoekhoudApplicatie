package com.example.nrfaboekhoudapplicatie.dal.implementatie;

import com.example.nrfaboekhoudapplicatie.DTO.*;
import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import com.example.nrfaboekhoudapplicatie.dal.entity.Client;
import com.example.nrfaboekhoudapplicatie.dal.entity.Role;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.dal.repository.ClientRepository;
import com.example.nrfaboekhoudapplicatie.dal.repository.UserRepository;
import com.example.nrfaboekhoudapplicatie.dal.repository.AccountantRepository;
import com.example.nrfaboekhoudapplicatie.service.RoleService;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IClientDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClientDAL implements IClientDAL {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final AccountantRepository accountantRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ClientDAL(ClientRepository clientRepository,
                     UserRepository userRepository,
                     AccountantRepository accountantRepository,
                     RoleService roleService,
                     BCryptPasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.accountantRepository = accountantRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ClientReadDTO create(ClientCreateDTO createDTO) {
        if (userRepository.existsByUsername(createDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(createDTO.getPassword());
        Role clientRole = roleService.getOrCreateRole("CLIENT");

        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setPassword(hashedPassword);
        user.getRoles().add(clientRole);

        User savedUser = userRepository.save(user);

        Accountant accountant = accountantRepository.findById(createDTO.getAccountantId())
                .orElseThrow(() -> new IllegalArgumentException("Accountant not found"));

        Client client = new Client();
        client.setFirstName(createDTO.getFirstName());
        client.setLastName(createDTO.getLastName());
        client.setEmail(createDTO.getEmail());
        client.setAccountant(accountant);
        client.setUser(savedUser);

        Client savedClient = clientRepository.save(client);

        return mapToReadDTO(savedClient);
    }

    @Override
    public Optional<ClientReadDTO> read(Long clientId) {
        return clientRepository.findById(clientId)
                .map(this::mapToReadDTO);
    }

    @Override
    public Optional<ClientReadDTO> readByUserId(Long userId) {
        return clientRepository.findByUserId(userId)
                .map(this::mapToReadDTO);
    }

    @Override
    public List<ClientReadDTO> readAll() {
        return clientRepository.findAll().stream()
                .map(this::mapToReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientReadDTO update(ClientUpdateDTO updateDTO) {
        Client client = clientRepository.findById(updateDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        User user = client.getUser();
        user.setUsername(updateDTO.getUsername());

        if (updateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        client.setFirstName(updateDTO.getFirstName());
        client.setLastName(updateDTO.getLastName());
        client.setEmail(updateDTO.getEmail());

        userRepository.save(user);
        Client updatedClient = clientRepository.save(client);

        return mapToReadDTO(updatedClient);
    }

    @Override
    public void delete(ClientDeleteDTO deleteDTO) {
        Client client = clientRepository.findById(deleteDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        clientRepository.delete(client);
    }

    private ClientReadDTO mapToReadDTO(Client client) {
        ClientReadDTO dto = new ClientReadDTO();
        dto.setId(client.getId());
        dto.setUsername(client.getUser().getUsername());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setAccountantId(client.getAccountant().getId());
        return dto;
    }
}
