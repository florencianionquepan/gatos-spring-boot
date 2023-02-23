package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioReqDTO;
import com.example.gatosspringboot.dto.UsuarioRespDTO;
import com.example.gatosspringboot.model.Usuario;

import java.util.List;

public interface IUsuarioMapper {
    Usuario mapToEntity(UsuarioReqDTO dto);
    UsuarioRespDTO mapToDto(Usuario entity);
    List<UsuarioRespDTO> mapListToDto(List<Usuario> usuarios);
}
