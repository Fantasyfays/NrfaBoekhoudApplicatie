package com.example.nrfaboekhoudapplicatie.presentation;

import com.example.nrfaboekhoudapplicatie.DTO.*;
import com.example.nrfaboekhoudapplicatie.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/accountant/{accountantId}")
    public ResponseEntity<ClientReadDTO> createClient(@PathVariable Long accountantId, @RequestBody ClientCreateDTO createDTO) {
        createDTO.setAccountantId(accountantId);
        ClientReadDTO createdClient = clientService.createClient(createDTO);
        return ResponseEntity.ok(createdClient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientReadDTO> getClientById(@PathVariable Long id) {
        Optional<ClientReadDTO> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientReadDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/accountant/{accountantId}")
    public ResponseEntity<List<ClientReadDTO>> getClientsByAccountantId(@PathVariable Long accountantId) {
        return ResponseEntity.ok(clientService.getClientsByAccountantId(accountantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientReadDTO> updateClient(@PathVariable Long id, @RequestBody ClientUpdateDTO updateDTO) {
        updateDTO.setId(id);
        ClientReadDTO updatedClient = clientService.updateClient(updateDTO);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        ClientDeleteDTO deleteDTO = new ClientDeleteDTO();
        deleteDTO.setId(id);
        clientService.deleteClient(deleteDTO);
        return ResponseEntity.noContent().build();
    }
}
