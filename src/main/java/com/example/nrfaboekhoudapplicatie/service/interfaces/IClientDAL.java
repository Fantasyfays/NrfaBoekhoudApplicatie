package com.example.nrfaboekhoudapplicatie.service.interfaces;

import com.example.nrfaboekhoudapplicatie.DTO.*;

import java.util.List;
import java.util.Optional;

public interface IClientDAL {

    ClientReadDTO create(ClientCreateDTO createDTO);

    Optional<ClientReadDTO> read(Long clientId);

    Optional<ClientReadDTO> readByUserId(Long userId);

    List<ClientReadDTO> readAll();

    ClientReadDTO update(ClientUpdateDTO updateDTO);

    void delete(ClientDeleteDTO deleteDTO);
}
