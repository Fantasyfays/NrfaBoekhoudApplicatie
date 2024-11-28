package com.example.nrfaboekhoudapplicatie;

import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientCreateDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.ClientResponseDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.Accountant;
import com.example.nrfaboekhoudapplicatie.dal.entity.Client;
import com.example.nrfaboekhoudapplicatie.service.ClientService;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IAccountantDAL;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IClientDAL;
import com.example.nrfaboekhoudapplicatie.service.dalInterfaces.IUserDAL;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceTest {

    // Mocks aanmaken
    private final IClientDAL clientDAL = mock(IClientDAL.class);
    private final IAccountantDAL accountantDAL = mock(IAccountantDAL.class);
    private final IUserDAL userDAL = mock(IUserDAL.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    // Service die we willen testen
    private final ClientService clientService = new ClientService(clientDAL, accountantDAL, userDAL, passwordEncoder);

    @Test
    public void testClientAanmaken() {
        // Stap 1: Geef input (ClientCreateDTO)
        ClientCreateDTO dto = new ClientCreateDTO(
                "John",                // Voornaam
                "Doe",                 // Achternaam
                "johndoe@example.com", // Email
                "+123456789",          // Telefoonnummer
                1L,                    // Accountant ID
                "john123",             // Gebruikersnaam
                "password"             // Wachtwoord
        );

        // Stap 2: Maak een mock accountant
        Accountant accountant = new Accountant();
        accountant.setId(1L);

        // Stel de mock afhankelijkheden in
        when(clientDAL.existsByEmail(dto.getEmail())).thenReturn(false); // Email bestaat niet
        when(userDAL.findByUsername(dto.getUsername())).thenReturn(Optional.empty()); // Gebruikersnaam bestaat niet
        when(accountantDAL.findById(dto.getAccountantId())).thenReturn(Optional.of(accountant)); // Accountant wordt gevonden

        // Maak een client die we opslaan
        Client savedClient = new Client();
        savedClient.setId(1L);
        savedClient.setFirstName("John");
        savedClient.setLastName("Doe");
        savedClient.setEmail("johndoe@example.com");
        savedClient.setPhoneNumber("+123456789");
        savedClient.setAccountant(accountant);

        // Laat de mock save() methode de client retourneren
        when(clientDAL.save(any(Client.class))).thenReturn(savedClient);

        // Stap 3: Act - roep de methode aan
        ClientResponseDTO response = clientService.createClient(dto);

        // Stap 4: Controleer de output
        assertNotNull(response); // De response mag niet null zijn
        assertEquals(1L, response.getId()); // ID moet 1 zijn
        assertEquals("John", response.getFirstName()); // Voornaam moet kloppen
        assertEquals("Doe", response.getLastName()); // Achternaam moet kloppen
        assertEquals("johndoe@example.com", response.getEmail()); // Email moet kloppen
        assertEquals(1L, response.getAccountantId()); // Accountant ID moet kloppen
    }

    @Test
    public void testClientAanmakenEmailBestaatAl() {
        // Stap 1: Geef input (ClientCreateDTO)
        ClientCreateDTO dto = new ClientCreateDTO(
                "John",
                "Doe",
                "johndoe@example.com",
                "+123456789",
                1L,
                "john123",
                "password"
        );

        // Mock dat het emailadres al bestaat
        when(clientDAL.existsByEmail(dto.getEmail())).thenReturn(true);

        // Stap 2: Act - probeer een client aan te maken
        Exception exception = assertThrows(IllegalArgumentException.class, () -> clientService.createClient(dto));

        // Stap 3: Controleer de output
        assertEquals("Email is already in use.", exception.getMessage());
    }

    @Test
    public void testClientAanmakenOngeldigTelefoonnummer() {
        // Stap 1: Geef input (ClientCreateDTO)
        ClientCreateDTO dto = new ClientCreateDTO(
                "John",
                "Doe",
                "johndoe@example.com",
                "invalid-phone", // Ongeldig telefoonnummer
                1L,
                "john123",
                "password"
        );

        // Mock afhankelijkheden
        when(clientDAL.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userDAL.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
        when(accountantDAL.findById(dto.getAccountantId())).thenReturn(Optional.of(new Accountant()));

        // Stap 2: Act - probeer een client aan te maken
        Exception exception = assertThrows(IllegalArgumentException.class, () -> clientService.createClient(dto));

        // Stap 3: Controleer de output
        assertEquals("Phone number must contain only digits.", exception.getMessage());
    }
}
