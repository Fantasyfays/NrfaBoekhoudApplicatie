package com.example.nrfaboekhoudapplicatie.DTO.Invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemDTO {
        private String description;
        private int quantity;
        private BigDecimal price;
        private BigDecimal vatRate;
        private String generalLedgerAccount;
    }