package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UserResponseDTO;
import com.example.gatosspringboot.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface IUserResponseMapper {
    UserResponseDTO mapToDTO(Usuario user);
}
