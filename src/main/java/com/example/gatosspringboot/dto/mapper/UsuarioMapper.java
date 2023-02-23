package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioReqDTO;
import com.example.gatosspringboot.dto.UsuarioRespDTO;
import com.example.gatosspringboot.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper implements IUsuarioMapper{
    @Autowired
    PasswordEncoder passwordEncored;

    @Override
    public Usuario mapToEntity(UsuarioReqDTO dto) {
        Usuario user=new Usuario();
        user.setMail(dto.getMail());
        user.setContrasenia(passwordEncored.encode(dto.getPassword()));
        return user;
    }

    @Override
    public UsuarioRespDTO mapToDto(Usuario entity) {
        UsuarioRespDTO dto=new UsuarioRespDTO();
        dto.setMail(entity.getMail());
        return dto;
    }

    @Override
    public List<UsuarioRespDTO> mapListToDto(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }
}
