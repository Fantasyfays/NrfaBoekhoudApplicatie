package com.example.nrfaboekhoudapplicatie.services;

import com.example.nrfaboekhoudapplicatie.DTO.Invoice.InvoiceDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.Invoice;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.service.AuthService;
import com.example.nrfaboekhoudapplicatie.service.InvoiceService;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IInvoiceDAL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    private IInvoiceDAL invoiceDAL;
    private AuthService authService;
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceDAL = mock(IInvoiceDAL.class);
        authService = mock(AuthService.class);
        invoiceService = new InvoiceService(invoiceDAL, authService);
    }

    @Test
    void getInvoicesForCurrentUser_HappyFlow() {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        when(authService.getAuthenticatedUser()).thenReturn(Optional.of(mockUser));

        Invoice mockInvoice = new Invoice();
        mockInvoice.setInvoiceNumber("INV001");
        mockInvoice.setUser(mockUser);
        when(invoiceDAL.findByUser(mockUser)).thenReturn(List.of(mockInvoice));

        List<InvoiceDTO> result = invoiceService.getInvoicesForCurrentUser();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("INV001", result.get(0).getInvoiceNumber());
        verify(authService, times(1)).getAuthenticatedUser();
        verify(invoiceDAL, times(1)).findByUser(mockUser);
    }

    @Test
    void getInvoicesForCurrentUser_UnhappyFlow() {
        when(authService.getAuthenticatedUser()).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> invoiceService.getInvoicesForCurrentUser());
        assertEquals("Geen ingelogde gebruiker gevonden", exception.getMessage());
        verify(authService, times(1)).getAuthenticatedUser();
        verifyNoInteractions(invoiceDAL);
    }

    @Test
    void getInvoicesForCurrentUser_EdgeCase_NoInvoices() {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        when(authService.getAuthenticatedUser()).thenReturn(Optional.of(mockUser));

        when(invoiceDAL.findByUser(mockUser)).thenReturn(Collections.emptyList());

        List<InvoiceDTO> result = invoiceService.getInvoicesForCurrentUser();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(authService, times(1)).getAuthenticatedUser();
        verify(invoiceDAL, times(1)).findByUser(mockUser);
    }
}
