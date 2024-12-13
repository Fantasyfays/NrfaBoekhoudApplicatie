package com.example.nrfaboekhoudapplicatie.DTO.Invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private String invoiceNumber;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String customerName;
    private String customerVATNumber;
    private String customerAddress;

    private List<InvoiceItemDTO> items;


}


