package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.SolicitudVoluntariadoDTO;
import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.EstadoNombre;
import com.example.gatosspringboot.model.SolicitudVoluntariado;
import com.example.gatosspringboot.model.TipoVoluntariado;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudVoluntariadoMapper implements ISolicitudVoluntariadoMapper{

    private final IPersonaMapper persoMapper;

    public SolicitudVoluntariadoMapper(IPersonaMapper persoMapper) {
        this.persoMapper = persoMapper;
    }

    @Override
    public SolicitudVoluntariado mapToEntity(SolicitudVoluntariadoDTO dto) {
        SolicitudVoluntariado entidad=new SolicitudVoluntariado();
        entidad.setId(dto.getId());
        entidad.setAspirante(this.persoMapper.mapToPersona(dto.getAspirante()));
        entidad.setTipoVoluntariado(TipoVoluntariado.valueOf(dto.getVoluntariado()));
        //dto a entidad no tiene estado
        entidad.setSocio(dto.getSocio());
        return entidad;
    }

    @Override
    public SolicitudVoluntariadoDTO mapToDto(SolicitudVoluntariado entity) {
        SolicitudVoluntariadoDTO dto=new SolicitudVoluntariadoDTO();
        dto.setId(entity.getId());
        dto.setAspirante(this.persoMapper.mapToDto(entity.getAspirante()));
        dto.setVoluntariado(entity.getTipoVoluntariado().name());
        dto.setEstados(entity.getEstados().stream()
                .map(e->e.getEstado().name())
                .collect(Collectors.toList()));
        dto.setSocio(entity.getSocio());
        return dto;
    }

    @Override
    public List<SolicitudVoluntariadoDTO> mapToListDto(List<SolicitudVoluntariado> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
