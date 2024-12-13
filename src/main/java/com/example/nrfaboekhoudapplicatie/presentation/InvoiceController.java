package com.example.nrfaboekhoudapplicatie.presentation;

import com.example.nrfaboekhoudapplicatie.DTO.Invoice.InvoiceDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.Invoice;
import com.example.nrfaboekhoudapplicatie.service.InvoiceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceService.createInvoice(invoiceDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("invoiceId", invoice.getId());
        response.put("invoiceNumber", invoice.getInvoiceNumber());
        response.put("status", invoice.getStatus());
        response.put("totalAmount", invoice.getTotalAmount());
        response.put("items", invoice.getItems());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/my")
    public ResponseEntity<List<InvoiceDTO>> getMyInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getInvoicesForCurrentUser();
        return ResponseEntity.ok(invoices);
    }


}
