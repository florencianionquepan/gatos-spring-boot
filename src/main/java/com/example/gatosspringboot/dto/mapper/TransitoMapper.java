package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.GatoIdDTO;
import com.example.gatosspringboot.dto.TransitoIdDTO;
import com.example.gatosspringboot.dto.TransitoRespDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Transito;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransitoMapper implements ITransitoMapper{

    @Override
    public TransitoRespDTO mapToDto(Transito entity) {
        TransitoRespDTO dto=new TransitoRespDTO();
        Persona perso=entity.getPersona();
        dto.setId(entity.getId());
        dto.setDni(perso.getDni());
        dto.setNombre(perso.getNombre());
        dto.setEmail(perso.getEmail());
        dto.setApellido(perso.getApellido());
        dto.setFechaNac(perso.getFechaNac());
        dto.setTel(perso.getTel());
        dto.setDire(perso.getDire());
        dto.setLocalidad(perso.getLocalidad());
        List<GatoIdDTO> gatos=new ArrayList<>();
        if(entity.getListaGatos() != null && !entity.getListaGatos().isEmpty()){
            List<GatoIdDTO> gatosIdDTO = entity.getListaGatos().stream()
                    .map(gato -> new GatoIdDTO(gato.getId()))
                    .collect(Collectors.toList());
            dto.setGatos(gatosIdDTO);
        }else{
            dto.setGatos(gatos);
        }
        return dto;
    }

    @Override
    public Transito mapToEntity(TransitoIdDTO dto) {
        Transito ent=new Transito();
        ent.setId(dto.getId());
        return ent;
    }

    @Override
    public List<TransitoRespDTO> mapToListDto(List<Transito> entities) {
        return entities.stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }
}
