package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.PersonaDTO;
import com.example.gatosspringboot.dto.UserResponseDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper implements IUserResponseMapper{

    @Override
    public UserResponseDTO mapToDTO(Usuario user, Persona perso) {
        UserResponseDTO dto=new UserResponseDTO();
        dto.setEmail(user.getEmail());
        dto.setNombre(perso.getNombre());
        dto.setLocalidad(perso.getLocalidad());
        dto.setRoles(user.getRoles());
        return dto;
    }
}
