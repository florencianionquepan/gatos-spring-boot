package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioUsuarioDTO;
import com.example.gatosspringboot.model.Voluntario;
import org.springframework.stereotype.Component;

@Component
public class VoluntarioUsuarioMapper implements IVoluntarioUsuarioMapper{

    private final IUsuarioEmailMapper userMap;

    public VoluntarioUsuarioMapper(IUsuarioEmailMapper userMap) {
        this.userMap = userMap;
    }

    @Override
    public Voluntario mapToEntity(VoluntarioUsuarioDTO dto) {
        Voluntario entidad=new Voluntario();
        entidad.setUsuario(this.userMap.mapToEntity(dto.getUsuario()));
        return entidad;
    }

    @Override
    public VoluntarioUsuarioDTO mapToDto(Voluntario entity) {
        VoluntarioUsuarioDTO dto=new VoluntarioUsuarioDTO();
        dto.setUsuario(this.userMap.mapToDto(entity.getUsuario()));
        return dto;
    }
}
