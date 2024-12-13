package com.example.nrfaboekhoudapplicatie.service.interfaces;

import com.example.nrfaboekhoudapplicatie.dal.entity.Invoice;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;

import java.util.List;
import java.util.Optional;

public interface IInvoiceDAL {
    Invoice save(Invoice invoice);

    Optional<Invoice> findById(Long id);

    List<Invoice> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
    List<Invoice> findByUser(User user);
}
