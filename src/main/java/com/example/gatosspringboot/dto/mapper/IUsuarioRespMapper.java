package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioRespDTO;
import com.example.gatosspringboot.model.Usuario;

public interface IUsuarioRespMapper {
    UsuarioRespDTO mapToDto(Usuario entity);
    Usuario mapToEntity(UsuarioRespDTO dto);
}
