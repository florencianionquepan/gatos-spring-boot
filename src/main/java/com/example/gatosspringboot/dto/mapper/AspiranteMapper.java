package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.AspiranteDTO;
import com.example.gatosspringboot.model.Aspirante;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AspiranteMapper implements IAspiranteMapper{

    @Override
    public Aspirante mapToEntity(AspiranteDTO dto) {
        Aspirante ent=new Aspirante();
        ent.setId(dto.getId());
        ent.setDni(dto.getDni());
        ent.setNombre(dto.getNombre());
        ent.setApellido(dto.getApellido());
        ent.setTel(dto.getTel());
        ent.setEmail(dto.getEmail());
        ent.setFechaNac(dto.getFechaNac());
        ent.setDire(dto.getDire());
        ent.setLocalidad(dto.getLocalidad());
        ent.setSolicitudes(dto.getSolicitudes());
        ent.setTiposVoluntariado(dto.getTiposVoluntariado());
        ent.setSocio(dto.getSocio());
        return ent;
    }

    @Override
    public AspiranteDTO mapToDto(Aspirante entity) {
        AspiranteDTO dto=new AspiranteDTO();
        dto.setId(entity.getId());
        dto.setDni(entity.getDni());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setTel(entity.getTel());
        dto.setEmail(entity.getEmail());
        dto.setFechaNac(entity.getFechaNac());
        dto.setDire(entity.getDire());
        dto.setLocalidad(entity.getLocalidad());
        dto.setSolicitudes(entity.getSolicitudes());
        dto.setTiposVoluntariado(entity.getTiposVoluntariado());
        dto.setSocio(entity.getSocio());
        return dto;
    }

    @Override
    public List<AspiranteDTO> mapToListDto(List<Aspirante> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
