package com.example.nrfaboekhoudapplicatie;

import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientCreateDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientResponseDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import com.example.nrfaboekhoudapplicatie.dal.entity.Client;
import com.example.nrfaboekhoudapplicatie.service.ClientService;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IAccountantDAL;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IClientDAL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private IClientDAL clientDAL; // Mock de IClientDAL in plaats van een repository

    @Mock
    private IAccountantDAL accountantDAL; // Mock de IAccountantDAL

    @InjectMocks
    private ClientService clientService; // Inject de mocks in de service

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Zorg ervoor dat de mocks correct geïnitialiseerd worden
    }

    @Test
    void testCreateClient_HappyFlow() {
        // ARRANGE
        ClientCreateDTO dto = new ClientCreateDTO("John", "Doe", "john.doe@example.com", "+123456789", 1L);
        Accountant accountant = new Accountant();
        accountant.setId(1L);

        Client client = new Client();
        client.setId(1L);
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setAccountant(accountant);

        when(accountantDAL.findById(1L)).thenReturn(Optional.of(accountant));
        when(clientDAL.existsByEmail(dto.getEmail())).thenReturn(false);
        when(clientDAL.save(any(Client.class))).thenReturn(client);

        // ACT
        ClientResponseDTO response = clientService.createClient(dto);

        // ASSERT
        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());
        verify(clientDAL, times(1)).save(any(Client.class));
    }

    @Test
    void testCreateClient_UnhappyFlow_EmailAlreadyExists() {
        // ARRANGE
        ClientCreateDTO dto = new ClientCreateDTO("John", "Doe", "john.doe@example.com", "+123456789", 1L);

        when(clientDAL.existsByEmail(dto.getEmail())).thenReturn(true);

        // ACT & ASSERT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.createClient(dto);
        });

        assertEquals("Email is already in use.", exception.getMessage());
        verify(clientDAL, never()).save(any(Client.class));
    }

    @Test
    void testCreateClient_EdgeCase_AccountantNotFound() {
        // ARRANGE
        ClientCreateDTO dto = new ClientCreateDTO("John", "Doe", "john.doe@example.com", "+123456789", 99L);

        when(accountantDAL.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            clientService.createClient(dto);
        });

        assertEquals("Accountant not found.", exception.getMessage());
        verify(clientDAL, never()).save(any(Client.class));
    }
}
