package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioPasswordDTO;
import com.example.gatosspringboot.model.Usuario;

public interface IUsuarioPasswordMapper {
    Usuario mapToEntity(UsuarioPasswordDTO dto);
}
