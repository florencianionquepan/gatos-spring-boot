package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.RolDTO;
import com.example.gatosspringboot.dto.UserResponseDTO;
import com.example.gatosspringboot.dto.UsuarioRespDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Rol;
import com.example.gatosspringboot.model.Usuario;

import java.util.HashMap;
import java.util.List;

public interface IUserResponseMapper {
    UserResponseDTO mapToDTO(Usuario user, Persona perso, boolean esTransito, boolean esPadrino);
    List<UsuarioRespDTO> mapToListDto(HashMap<Usuario,Persona> hashmap);
}
