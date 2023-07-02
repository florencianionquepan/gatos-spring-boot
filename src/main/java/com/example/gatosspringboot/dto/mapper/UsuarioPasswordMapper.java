package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioPasswordDTO;
import com.example.gatosspringboot.model.Usuario;

public class UsuarioPasswordMapper implements IUsuarioPasswordMapper{
    @Override
    public Usuario mapToEntity(UsuarioPasswordDTO dto) {
        Usuario entity=new Usuario();
        entity.setMail(dto.getMail());
        entity.setContrasenia(dto.getPasswordNew());
        return entity;
    }
}
