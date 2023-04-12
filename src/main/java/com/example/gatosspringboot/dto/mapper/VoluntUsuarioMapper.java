package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioDTO;
import com.example.gatosspringboot.dto.VoluntarioUsuarioDTO;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.model.Voluntario;
import org.springframework.stereotype.Component;

@Component
public class VoluntUsuarioMapper implements IVoluntUsuarioMapper{

    private final IUsuarioRespMapper usuarioMapper;

    public VoluntUsuarioMapper(IUsuarioRespMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Voluntario mapToEntity(VoluntarioUsuarioDTO dto) {
        Voluntario entity=new Voluntario();
        Usuario usuario=this.usuarioMapper.mapToEntity(dto.getUsuario());
        entity.setUsuario(usuario);
        return entity;
    }

    @Override
    public VoluntarioDTO mapToDto(Voluntario entity) {
        VoluntarioDTO dto=new VoluntarioDTO();
        dto.setUsuario(this.usuarioMapper.mapToDto(entity.getUsuario()));
        return dto;
    }
}
