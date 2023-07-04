package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioEmailDTO;
import com.example.gatosspringboot.model.Voluntario;
import org.springframework.stereotype.Component;

@Component
public class VoluntarioEmailMapper implements IVoluntarioEmailMapper {

    @Override
    public Voluntario mapToEntity(VoluntarioEmailDTO dto) {
        Voluntario entidad=new Voluntario();
        entidad.setEmail(dto.getEmail());
        return entidad;
    }

    @Override
    public VoluntarioEmailDTO mapToDto(Voluntario entity) {
        VoluntarioEmailDTO dto=new VoluntarioEmailDTO();
        dto.setEmail(entity.getEmail());
        return dto;
    }
}
