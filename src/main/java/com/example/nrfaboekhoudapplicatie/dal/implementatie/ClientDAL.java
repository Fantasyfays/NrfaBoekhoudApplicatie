package com.example.nrfaboekhoudapplicatie.dal.implementatie;

import com.example.nrfaboekhoudapplicatie.dal.entity.Client;
import com.example.nrfaboekhoudapplicatie.dal.repository.ClientRepository;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IClientDAL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClientDAL implements IClientDAL {

    private final ClientRepository clientRepository;

    public ClientDAL(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }
    @Override
    public boolean existsById(Long id) {
        return clientRepository.existsById(id); // Implementatie toegevoegd
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public List<Client> findByAccountantId(Long accountantId) {
        return clientRepository.findByAccountantId(accountantId);
    }
}
