package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UsuarioReqDTO;
import com.example.gatosspringboot.dto.UsuarioEmailDTO;
import com.example.gatosspringboot.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper implements IUsuarioMapper{

    @Override
    public Usuario mapToEntity(UsuarioReqDTO dto) {
        Usuario user=new Usuario();
        user.setEmail(dto.getEmail());
        user.setContrasenia(dto.getPassword());
        return user;
    }

    @Override
    public UsuarioEmailDTO mapToDto(Usuario entity) {
        UsuarioEmailDTO dto=new UsuarioEmailDTO();
        dto.setEmail(entity.getEmail());
        return dto;
    }

    @Override
    public List<UsuarioEmailDTO> mapListToDto(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }
}
