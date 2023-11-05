package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.*;
import com.example.gatosspringboot.model.*;
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
        GatoRespDTO gatodto=new GatoRespDTO();
        gatodto.setId(entity.getGato().getId());
        gatodto.setNombre(entity.getGato().getNombre());
        gatodto.setFotos(entity.getGato().getFotos().stream()
                .map(Foto::getFotoUrl).collect(Collectors.toList()));
        gatodto.setAdoptado(entity.getGato().getAdoptadoFecha());
        if(entity.getGato().getAsignacionesTransitos()!=null &&
                !entity.getGato().getAsignacionesTransitos().isEmpty()){
            List<Transito> transitos=entity.getGato().getAsignacionesTransitos().stream()
                    .map(GatoTransito::getTransito)
                    .collect(Collectors.toList());
            Transito ultimo=transitos.get(transitos.size()-1);
            TransitoRespDTO tranDTO=new TransitoRespDTO();
            tranDTO.setNombre(ultimo.getPersona().getNombre());
            tranDTO.setLocalidad(ultimo.getPersona().getLocalidad());
            gatodto.setTransito(tranDTO);
        }
        Voluntario volu=entity.getGato().getVoluntario();
        VoluntarioDTO voluDTO=new VoluntarioDTO();
        voluDTO.setNombre(volu.getPersona().getNombre());
        voluDTO.setLocalidad(volu.getPersona().getLocalidad());
        gatodto.setVoluntario(voluDTO);
        dto.setGato(gatodto);
        return dto;
    }

    @Override
    public List<SolicitudRespDTO> mapListToDto(List<SolicitudAdopcion> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
