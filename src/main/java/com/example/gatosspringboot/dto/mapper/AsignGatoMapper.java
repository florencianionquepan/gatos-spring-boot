package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.AsignacionGatoDTO;
import com.example.gatosspringboot.dto.AsignacionTransitoDTO;
import com.example.gatosspringboot.model.GatoTransito;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AsignGatoMapper implements IAsignGatoMapper{

    private final IGatoMapper gatoMap;
    private final IPersonaMapper persoMap;

    public AsignGatoMapper(IGatoMapper gatoMap,
                           IPersonaMapper persoMap) {
        this.gatoMap = gatoMap;
        this.persoMap = persoMap;
    }

    @Override
    public AsignacionGatoDTO mapToDto(GatoTransito entity) {
        AsignacionGatoDTO dto=new AsignacionGatoDTO();
        dto.setId(entity.getId());
        dto.setFechaAsignacion(entity.getFechaAsociacion());
        if(entity.getFechaFin()!=null) {
            dto.setFechaFin(entity.getFechaFin());
        }
        dto.setGato(this.gatoMap.mapToDtoSimple(entity.getGato()));
        return dto;
    }

    @Override
    public AsignacionTransitoDTO mapToDtoTransito(GatoTransito entity) {
        AsignacionTransitoDTO dto=new AsignacionTransitoDTO();
        dto.setId(entity.getId());
        dto.setFechaAsignacion(entity.getFechaAsociacion());
        if(entity.getFechaFin()!=null) {
            dto.setFechaFin(entity.getFechaFin());
        }
        dto.setTransito(this.persoMap.mapToDto(entity.getTransito().getPersona()));
        return dto;
    }

    @Override
    public List<AsignacionGatoDTO> mapToListDto(List<GatoTransito> asignacionesGatos) {
        return asignacionesGatos.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AsignacionTransitoDTO> mapToListDtoTransito(List<GatoTransito> asignaciones) {
        return asignaciones.stream()
                .map(this::mapToDtoTransito)
                .collect(Collectors.toList());
    }
}
