package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.PersonaEmailDTO;
import com.example.gatosspringboot.dto.SolicitudVoluntariadoDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.SolicitudVoluntariado;
import com.example.gatosspringboot.model.TipoVoluntariado;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudVoluntariadoMapper implements ISolicitudVoluntariadoMapper{

    private final ISocioMapper socioMapper;
    private final IEstadoMapper estadoMapper;
    private final IPersonaMapper persoMapper;

    public SolicitudVoluntariadoMapper(ISocioMapper socioMapper,
                                       IEstadoMapper estadoMapper,
                                       IPersonaMapper persoMapper) {
        this.socioMapper = socioMapper;
        this.estadoMapper = estadoMapper;
        this.persoMapper = persoMapper;
    }

    @Override
    public SolicitudVoluntariado mapToEntity(SolicitudVoluntariadoDTO dto) {
        SolicitudVoluntariado entidad=new SolicitudVoluntariado();
        entidad.setId(dto.getId());
        if(dto.getAspirante()!=null){
            Persona perso=new Persona();
            perso.setEmail(dto.getAspirante().getEmail());
            entidad.setAspirante(perso);
        }
        entidad.setTipoVoluntariado(TipoVoluntariado.valueOf(dto.getVoluntariado()));
        //dto a entidad no tiene estado
        entidad.setSocio(this.socioMapper.mapToEntity(dto.getSocio()));
        return entidad;
    }

    @Override
    public SolicitudVoluntariado mapToEntityForPut(SolicitudVoluntariadoDTO dto) {
        SolicitudVoluntariado entidad=new SolicitudVoluntariado();
        entidad.setId(dto.getId());
        if(dto.getAspirante()!=null){
            Persona perso=new Persona();
            perso.setEmail(dto.getAspirante().getEmail());
            entidad.setAspirante(perso);
        }
        //dto a entidad no tiene estado
        entidad.setSocio(this.socioMapper.mapToEntity(dto.getSocio()));
        return entidad;
    }

    @Override
    public SolicitudVoluntariadoDTO mapToDto(SolicitudVoluntariado entity) {
        SolicitudVoluntariadoDTO dto=new SolicitudVoluntariadoDTO();
        dto.setId(entity.getId());
        dto.setAspirante(this.persoMapper.mapToDto(entity.getAspirante()));
        dto.setVoluntariado(entity.getTipoVoluntariado().name());
        dto.setEstados(this.estadoMapper.mapToListDto(entity.getEstados()));
        if(entity.getSocio()!=null){
            dto.setSocio(this.socioMapper.mapToDto(entity.getSocio()));
        }
        return dto;
    }

    @Override
    public List<SolicitudVoluntariadoDTO> mapToListDto(List<SolicitudVoluntariado> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
