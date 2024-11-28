package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientCreateDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientResponseDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientUpdateDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import com.example.nrfaboekhoudapplicatie.dal.entity.Client;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.enums.RoleType;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IAccountantDAL;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IClientDAL;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IUserDAL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final IClientDAL clientDAL;
    private final IAccountantDAL accountantDAL;
    private final IUserDAL userDAL;
    private final PasswordEncoder passwordEncoder;

    public ClientService(IClientDAL clientDAL, IAccountantDAL accountantDAL, IUserDAL userDAL, PasswordEncoder passwordEncoder) {
        this.clientDAL = clientDAL;
        this.accountantDAL = accountantDAL;
        this.userDAL = userDAL;
        this.passwordEncoder = passwordEncoder;
    }

    public ClientResponseDTO createClient(ClientCreateDTO dto) {
        // Controleer of email al in gebruik is
        if (clientDAL.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        // Controleer of gebruikersnaam al in gebruik is
        if (userDAL.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use.");
        }

        // Haal de accountant op
        Accountant accountant = accountantDAL.findById(dto.getAccountantId())
                .orElseThrow(() -> new NoSuchElementException("Accountant not found."));

        // Controleer of het telefoonnummer correct is
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().matches("^\\+?[0-9]*$")) {
            throw new IllegalArgumentException("Phone number must contain only digits.");
        }

        // Maak de User aan
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Hash het wachtwoord
        user.setRoles(new HashSet<>(Collections.singleton(RoleType.CLIENT))); // Voeg CLIENT rol toe

        // Maak de Client aan en koppel de User
        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setAccountant(accountant);
        client.setUser(user); // Koppel de User aan de Client

        // Sla de Client op (User wordt automatisch opgeslagen door CascadeType.ALL)
        Client savedClient = clientDAL.save(client);

        // Retourneer de ResponseDTO
        return new ClientResponseDTO(
                savedClient.getId(),
                savedClient.getFirstName(),
                savedClient.getLastName(),
                savedClient.getEmail(),
                savedClient.getPhoneNumber(),
                savedClient.getAccountant().getId()
        );
    }


    public Optional<ClientResponseDTO> getClientById(Long id) {
        return clientDAL.findById(id)
                .map(client -> new ClientResponseDTO(
                        client.getId(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getEmail(),
                        client.getPhoneNumber(),
                        client.getAccountant().getId()
                ));
    }

    public List<ClientResponseDTO> getAllClients() {
        return clientDAL.findAll().stream()
                .map(client -> new ClientResponseDTO(
                        client.getId(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getEmail(),
                        client.getPhoneNumber(),
                        client.getAccountant().getId()
                ))
                .collect(Collectors.toList());
    }

    public Optional<ClientResponseDTO> updateClient(Long id, ClientUpdateDTO dto) {
        return clientDAL.findById(id).map(client -> {
            if (dto.getFirstName() != null) {
                client.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                client.setLastName(dto.getLastName());
            }
            if (dto.getEmail() != null) {
                if (!dto.getEmail().equals(client.getEmail()) && clientDAL.existsByEmail(dto.getEmail())) {
                    throw new IllegalArgumentException("Email is already in use.");
                }
                client.setEmail(dto.getEmail());
            }
            if (dto.getPhoneNumber() != null) {
                if (!dto.getPhoneNumber().matches("^\\+?[0-9]*$")) {
                    throw new IllegalArgumentException("Phone number must contain only digits.");
                }
                client.setPhoneNumber(dto.getPhoneNumber());
            }

            // Update de Client en sla op
            Client updatedClient = clientDAL.save(client);

            return new ClientResponseDTO(
                    updatedClient.getId(),
                    updatedClient.getFirstName(),
                    updatedClient.getLastName(),
                    updatedClient.getEmail(),
                    updatedClient.getPhoneNumber(),
                    updatedClient.getAccountant().getId()
            );
        });
    }


    public void deleteClient(Long id) {
        if (!clientDAL.existsById(id)) {
            throw new NoSuchElementException("Client not found.");
        }

        clientDAL.deleteById(id);
    }

}
