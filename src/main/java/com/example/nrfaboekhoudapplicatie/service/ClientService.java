package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientCreateDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientResponseDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientUpdateDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import com.example.nrfaboekhoudapplicatie.dal.entity.Client;
import com.example.nrfaboekhoudapplicatie.dal.repository.AccountantRepository;
import com.example.nrfaboekhoudapplicatie.dal.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountantRepository accountantRepository;

    public ClientService(ClientRepository clientRepository, AccountantRepository accountantRepository) {
        this.clientRepository = clientRepository;
        this.accountantRepository = accountantRepository;
    }

    public ClientResponseDTO createClient(ClientCreateDTO dto) {
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        Accountant accountant = accountantRepository.findById(dto.getAccountantId())
                .orElseThrow(() -> new NoSuchElementException("Accountant not found."));

        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setAccountant(accountant);

        Client savedClient = clientRepository.save(client);

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
        return clientRepository.findById(id)
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
        return clientRepository.findAll().stream()
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
        return clientRepository.findById(id).map(client -> {
            if (dto.getFirstName() != null) {
                client.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                client.setLastName(dto.getLastName());
            }
            if (dto.getEmail() != null) {
                if (!dto.getEmail().equals(client.getEmail()) && clientRepository.existsByEmail(dto.getEmail())) {
                    throw new IllegalArgumentException("Email is already in use.");
                }
                client.setEmail(dto.getEmail());
            }
            if (dto.getPhoneNumber() != null) {
                client.setPhoneNumber(dto.getPhoneNumber());
            }

            Client updatedClient = clientRepository.save(client);
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
        if (!clientRepository.existsById(id)) {
            throw new NoSuchElementException("Client not found.");
        }
        clientRepository.deleteById(id);
    }
}
