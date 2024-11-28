package com.example.nrfaboekhoudapplicatie.presentation;

import com.example.nrfaboekhoudapplicatie.dal.DTO.AccountantCreateDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.AccountantResponseDTO;
import com.example.nrfaboekhoudapplicatie.dal.DTO.AccountantUpdateDTO;
import com.example.nrfaboekhoudapplicatie.service.AccountantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accountants")
public class AccountantController {

    private final AccountantService accountantService;

    public AccountantController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountantResponseDTO> createAccountant(@Valid @RequestBody AccountantCreateDTO dto) {
        return new ResponseEntity<>(accountantService.createAccountant(dto), HttpStatus.CREATED);
    }


    @GetMapping("/view/{id}")
    public ResponseEntity<AccountantResponseDTO> getAccountant(@PathVariable long id){
        return new ResponseEntity<>(accountantService.getAccountantById(id), HttpStatus.OK);
    }
    @GetMapping("/list")
    public ResponseEntity<List<AccountantResponseDTO>> getAllAccountants() {
        return new ResponseEntity<>(accountantService.getAllAccountants(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AccountantResponseDTO> updateAccountant(
            @PathVariable Long id,
            @RequestBody AccountantUpdateDTO dto) {
        return new ResponseEntity<>(accountantService.updateAccountant(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccountant(@PathVariable Long id) {
        accountantService.deleteAccountant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
