package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioEmailDTO;
import com.example.gatosspringboot.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEmailMapper implements IUsuarioEmailMapper {
    @Override
    public UsuarioEmailDTO mapToDto(Usuario entity) {
        UsuarioEmailDTO dto=new UsuarioEmailDTO();
        dto.setMail(entity.getMail());
        return dto;
    }

    @Override
    public Usuario mapToEntity(UsuarioEmailDTO dto) {
        Usuario entity=new Usuario();
        entity.setMail(dto.getMail());
        return entity;
    }
}
