package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioRespDTO;
import com.example.gatosspringboot.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioRespMapper implements IUsuarioRespMapper{
    @Override
    public UsuarioRespDTO mapToDto(Usuario entity) {
        UsuarioRespDTO dto=new UsuarioRespDTO();
        dto.setMail(entity.getMail());
        return dto;
    }

    @Override
    public Usuario mapToEntity(UsuarioRespDTO dto) {
        Usuario entity=new Usuario();
        entity.setMail(dto.getMail());
        return entity;
    }
}
