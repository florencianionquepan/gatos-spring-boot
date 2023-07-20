package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.SolicitudDTO;
import com.example.gatosspringboot.model.Solicitud;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudMapper implements ISolicitudMapper{

    private final IPersonaMapper persoMap;
    private final IGatoIdMapper gatoMap;

    public SolicitudMapper(IPersonaMapper persoMap,
                           IGatoIdMapper gatoMap) {
        this.persoMap = persoMap;
        this.gatoMap = gatoMap;
    }

    @Override
    public Solicitud mapToEntity(SolicitudDTO dto) {
        Solicitud entity=new Solicitud();
        entity.setSolicitante(this.persoMap.mapToPersona(dto.getSolicitante()));
        entity.setGato(this.gatoMap.mapToEntity(dto.getGato()));
        return entity;
    }

    @Override
    public SolicitudDTO mapToDto(Solicitud entity) {
        SolicitudDTO dto=new SolicitudDTO();
        dto.setId(entity.getId());
        dto.setEstados(entity.getEstados());
        dto.setSolicitante(this.persoMap.mapToDto(entity.getSolicitante()));
        dto.setGato(this.gatoMap.mapToDto(entity.getGato()));
        return dto;
    }

    @Override
    public List<SolicitudDTO> mapListToDto(List<Solicitud> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
