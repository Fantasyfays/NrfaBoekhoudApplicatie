package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.DTO.Invoice.InvoiceDTO;
import com.example.nrfaboekhoudapplicatie.DTO.Invoice.InvoiceItemDTO;
import com.example.nrfaboekhoudapplicatie.dal.entity.Invoice;
import com.example.nrfaboekhoudapplicatie.dal.entity.InvoiceItem;
import com.example.nrfaboekhoudapplicatie.dal.entity.InvoiceStatus;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IInvoiceDAL;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final IInvoiceDAL invoiceDAL;
    private final AuthService authService;

    public InvoiceService(IInvoiceDAL invoiceDAL, AuthService authService) {
        this.invoiceDAL = invoiceDAL;
        this.authService = authService;
    }

    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceDAL.findAll();

        return invoices.stream().map(invoice -> {
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());
            invoiceDTO.setIssueDate(invoice.getIssueDate());
            invoiceDTO.setDueDate(invoice.getDueDate());
            invoiceDTO.setCustomerName(invoice.getCustomerName());
            invoiceDTO.setCustomerVATNumber(invoice.getCustomerVATNumber());
            invoiceDTO.setCustomerAddress(invoice.getCustomerAddress());

            List<InvoiceItemDTO> itemDTOs = invoice.getItems().stream().map(item -> {
                InvoiceItemDTO itemDTO = new InvoiceItemDTO();
                itemDTO.setDescription(item.getDescription());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setVatRate(item.getVatRate());
                itemDTO.setGeneralLedgerAccount(item.getGeneralLedgerAccount());
                return itemDTO;
            }).collect(Collectors.toList());

            invoiceDTO.setItems(itemDTOs);

            return invoiceDTO;
        }).collect(Collectors.toList());
    }

    public Invoice createInvoice(InvoiceDTO invoiceDTO) {
        User currentUser = authService.getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Geen ingelogde gebruiker gevonden"));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setCustomerName(invoiceDTO.getCustomerName());
        invoice.setCustomerVATNumber(invoiceDTO.getCustomerVATNumber());
        invoice.setCustomerAddress(invoiceDTO.getCustomerAddress());
        invoice.setStatus(InvoiceStatus.OPEN);
        invoice.setUser(currentUser);

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal vatTotal = BigDecimal.ZERO;

        for (InvoiceItemDTO itemDTO : invoiceDTO.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemDTO.getDescription());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setVatRate(itemDTO.getVatRate());

            BigDecimal itemTotal = itemDTO.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            BigDecimal itemVatAmount = itemTotal.multiply(itemDTO.getVatRate().divide(BigDecimal.valueOf(100)));

            item.setVatAmount(itemVatAmount);
            item.setTotal(itemTotal.add(itemVatAmount));
            item.setGeneralLedgerAccount(itemDTO.getGeneralLedgerAccount());
            item.setInvoice(invoice);

            invoice.getItems().add(item);

            subTotal = subTotal.add(itemTotal);
            vatTotal = vatTotal.add(itemVatAmount);
        }

        invoice.setSubTotal(subTotal);
        invoice.setVatTotal(vatTotal);
        invoice.setTotalAmount(subTotal.add(vatTotal));

        return invoiceDAL.save(invoice);
    }

    public List<InvoiceDTO> getInvoicesForCurrentUser() {
        User currentUser = authService.getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Geen ingelogde gebruiker gevonden"));

        List<Invoice> userInvoices = invoiceDAL.findByUser(currentUser);

        return userInvoices.stream().map(invoice -> {
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());
            invoiceDTO.setIssueDate(invoice.getIssueDate());
            invoiceDTO.setDueDate(invoice.getDueDate());
            invoiceDTO.setCustomerName(invoice.getCustomerName());
            invoiceDTO.setCustomerVATNumber(invoice.getCustomerVATNumber());
            invoiceDTO.setCustomerAddress(invoice.getCustomerAddress());

            List<InvoiceItemDTO> itemDTOs = invoice.getItems().stream().map(item -> {
                InvoiceItemDTO itemDTO = new InvoiceItemDTO();
                itemDTO.setDescription(item.getDescription());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setVatRate(item.getVatRate());
                itemDTO.setGeneralLedgerAccount(item.getGeneralLedgerAccount());
                return itemDTO;
            }).collect(Collectors.toList());

            invoiceDTO.setItems(itemDTOs);

            return invoiceDTO;
        }).collect(Collectors.toList());
    }

}
