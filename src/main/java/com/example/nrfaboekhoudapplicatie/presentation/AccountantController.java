package com.example.nrfaboekhoudapplicatie.presentation;

import com.example.nrfaboekhoudapplicatie.DTO.*;
import com.example.nrfaboekhoudapplicatie.service.AccountantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/accountants")
public class AccountantController {

    private final AccountantService accountantService;

    @Autowired
    public AccountantController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping
    public ResponseEntity<AccountantReadDTO> createAccountant(@RequestBody AccountantCreateDTO createDTO) {
        AccountantReadDTO createdAccountant = accountantService.createAccountant(createDTO);
        return ResponseEntity.ok(createdAccountant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountantReadDTO> getAccountantById(@PathVariable Long id) {
        Optional<AccountantReadDTO> accountant = accountantService.getAccountantById(id);
        return accountant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<AccountantReadDTO> updateAccountant(@RequestBody AccountantUpdateDTO updateDTO) {
        AccountantReadDTO updatedAccountant = accountantService.updateAccountant(updateDTO);
        return ResponseEntity.ok(updatedAccountant);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccountant(@RequestBody AccountantDeleteDTO deleteDTO) {
        accountantService.deleteAccountant(deleteDTO);
        return ResponseEntity.noContent().build();
    }
}
