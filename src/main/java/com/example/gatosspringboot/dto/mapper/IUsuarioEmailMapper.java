package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioEmailDTO;
import com.example.gatosspringboot.model.Usuario;

public interface IUsuarioEmailMapper {
    UsuarioEmailDTO mapToDto(Usuario entity);
    Usuario mapToEntity(UsuarioEmailDTO dto);
}
