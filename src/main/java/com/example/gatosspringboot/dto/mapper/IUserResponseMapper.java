package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UserResponseDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;

public interface IUserResponseMapper {
    UserResponseDTO mapToDTO(Usuario user, Persona perso, boolean esTransito, boolean esPadrino);
}
