package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.DTO.*;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.exceptions.UsernameAlreadyExistsException;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IClientDAL;
import com.example.nrfaboekhoudapplicatie.dal.implementatie.UserDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final IClientDAL clientDAL;
    private final UserDAL userDAL;

    @Autowired
    public ClientService(IClientDAL clientDAL, UserDAL userDAL) {
        this.clientDAL = clientDAL;
        this.userDAL = userDAL;
    }

    public ClientReadDTO createClient(ClientCreateDTO createDTO) {
        if (userDAL.existsByUsername(createDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        return clientDAL.create(createDTO);
    }

    public Optional<ClientReadDTO> getClientById(Long clientId) {
        System.out.println("Fetching client details for client ID: " + clientId);
        Optional<ClientReadDTO> client = clientDAL.read(clientId);
        System.out.println("Client details fetched: " + client);
        return client;
    }

    public Optional<ClientReadDTO> getClientByUserId(Long userId) {
        System.out.println("Fetching client details for user ID: " + userId);
        return clientDAL.readByUserId(userId); // Gebruik de nieuwe DAL-methode
    }

    public Optional<ClientReadDTO> getClientByUsername(String username) {
        System.out.println("Fetching user details for username: " + username);
        Optional<User> user = userDAL.findByUsername(username);

        if (user.isPresent()) {
            Long userId = user.get().getId();
            System.out.println("User found with ID: " + userId);
            return getClientByUserId(userId);
        }
        System.out.println("No user found for username: " + username);
        return Optional.empty();
    }

    public List<ClientReadDTO> getAllClients() {
        return clientDAL.readAll();
    }

    public ClientReadDTO updateClient(ClientUpdateDTO updateDTO) {
        if (userDAL.existsByUsername(updateDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        return clientDAL.update(updateDTO);
    }

    public void deleteClient(ClientDeleteDTO deleteDTO) {
        clientDAL.delete(deleteDTO);
    }

    public List<ClientReadDTO> getClientsByAccountantId(Long accountantId) {
        return clientDAL.readAll().stream()
                .filter(client -> client.getAccountantId().equals(accountantId))
                .toList();
    }
}
