package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioEmailDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Voluntario;
import org.springframework.stereotype.Component;

@Component
public class VoluntarioEmailMapper implements IVoluntarioEmailMapper {

    @Override
    public Voluntario mapToEntity(VoluntarioEmailDTO dto) {
        Voluntario entidad=new Voluntario();
        Persona perso=new Persona();
        perso.setEmail(dto.getEmail());
        entidad.setPersona(perso);
        return entidad;
    }

    @Override
    public VoluntarioEmailDTO mapToDto(Voluntario entity) {
        VoluntarioEmailDTO dto=new VoluntarioEmailDTO();
        dto.setEmail(entity.getPersona().getEmail());
        return dto;
    }
}
