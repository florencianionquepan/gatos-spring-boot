package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioReqDTO;
import com.example.gatosspringboot.dto.UsuarioEmailDTO;
import com.example.gatosspringboot.model.Usuario;

import java.util.List;

public interface IUsuarioMapper {
    Usuario mapToEntity(UsuarioReqDTO dto);
    UsuarioEmailDTO mapToDto(Usuario entity);
    List<UsuarioEmailDTO> mapListToDto(List<Usuario> usuarios);
}
