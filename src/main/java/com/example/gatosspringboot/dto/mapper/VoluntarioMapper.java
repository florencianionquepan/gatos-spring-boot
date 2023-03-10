package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioDTO;
import com.example.gatosspringboot.model.Voluntario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VoluntarioMapper implements IVoluntarioMapper{
    @Override
    public Voluntario mapToEntity(VoluntarioDTO volu) {
        Voluntario ent=new Voluntario();
        ent.setId(volu.getId());
        ent.setDni(volu.getDni());
        ent.setNombre(volu.getNombre());
        ent.setApellido(volu.getApellido());
        ent.setTel(volu.getTel());
        ent.setEmail(volu.getEmail());
        ent.setFechaNac(volu.getFechaNac());
        ent.setDire(volu.getDire());
        ent.setLocalidad(volu.getLocalidad());
        //ent.setListaGatos(volu.getGatitos());
        return ent;
    }

    @Override
    public VoluntarioDTO mapToDto(Voluntario entity) {
        VoluntarioDTO dto=new VoluntarioDTO();
        dto.setId(entity.getId());
        dto.setDni(entity.getDni());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setTel(entity.getTel());
        dto.setEmail(entity.getEmail());
        dto.setFechaNac(entity.getFechaNac());
        dto.setDire(entity.getDire());
        dto.setLocalidad(entity.getLocalidad());
        //dto.setGatitos(entity.);
        return dto;
    }

    @Override
    public List<VoluntarioDTO> mapToListDto(List<Voluntario> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
