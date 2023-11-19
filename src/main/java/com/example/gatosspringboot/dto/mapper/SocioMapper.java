package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.SocioDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Socio;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SocioMapper implements ISocioMapper {
    @Override
    public SocioDTO mapToDto(Socio entity) {
        SocioDTO dto=new SocioDTO();
        Persona perso=entity.getPersona();
        dto.setId(entity.getId());
        dto.setNombre(perso.getNombre());
        dto.setApellido(perso.getApellido());
        dto.setDni(perso.getDni());
        dto.setEmail(perso.getEmail());
        dto.setFechaNac(perso.getFechaNac());
        dto.setTel(perso.getTel());
        dto.setDire(perso.getDire());
        dto.setLocalidad(perso.getLocalidad());
        return dto;
    }

    @Override
    public Socio mapToEntity(SocioDTO dto) {
        Socio socio=new Socio();
        Persona perso=new Persona();
        perso.setEmail(dto.getEmail());
        socio.setPersona(perso);
        return socio;
    }

    @Override
    public List<SocioDTO> mapToListDto(List<Socio> entities) {
        return entities.stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }
}
