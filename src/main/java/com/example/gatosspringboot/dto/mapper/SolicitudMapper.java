package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.PersonaDTO;
import com.example.gatosspringboot.dto.SolicitudReqDTO;
import com.example.gatosspringboot.dto.SolicitudRespDTO;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.SolicitudAdopcion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudMapper implements ISolicitudMapper{

    private final IPersonaMapper persoMap;
    private final IGatoMapper gatoMap;
    private final IEstadoMapper estadoMapper;

    public SolicitudMapper(IPersonaMapper persoMap,
                           IGatoMapper gatoMap,
                           IEstadoMapper estadoMapper) {
        this.persoMap = persoMap;
        this.gatoMap = gatoMap;
        this.estadoMapper = estadoMapper;
    }

    @Override
    public SolicitudAdopcion mapToEntity(SolicitudReqDTO dto) {
        SolicitudAdopcion entity=new SolicitudAdopcion();
        Persona solicitante=new Persona();
        solicitante.setEmail(dto.getSolicitante().getEmail());
        entity.setSolicitante(solicitante);
        Gato gato=new Gato();
        gato.setId(dto.getGato().getId());
        entity.setGato(gato);
        return entity;
    }

    @Override
    public SolicitudRespDTO mapToDto(SolicitudAdopcion entity) {
        SolicitudRespDTO dto=new SolicitudRespDTO();
        dto.setId(entity.getId());
        dto.setEstados(this.estadoMapper.mapToListDto(entity.getEstados()));
        PersonaDTO personaDTO=this.persoMap.mapToDto(entity.getSolicitante());
        dto.setSolicitante(personaDTO);
        dto.setGato(this.gatoMap.mapToDto(entity.getGato()));
        return dto;
    }

    @Override
    public List<SolicitudRespDTO> mapListToDto(List<SolicitudAdopcion> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
