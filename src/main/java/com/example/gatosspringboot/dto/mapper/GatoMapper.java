package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.GatoDTO;
import com.example.gatosspringboot.dto.PersonaEmailDTO;
import com.example.gatosspringboot.dto.SolicitudReqDTO;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.SolicitudAdopcion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GatoMapper implements IGatoMapper{

    private final IVoluntarioEmailMapper volMap;
    private final IFichaMapper fichaMap;
    private final IEstadoMapper estadoMapper;

    public GatoMapper(IVoluntarioEmailMapper volMap,
                      IFichaMapper fichaMap,
                      IEstadoMapper estadoMapper) {
        this.volMap = volMap;
        this.fichaMap = fichaMap;
        this.estadoMapper = estadoMapper;
    }


    @Override
    public Gato mapToEntity(GatoDTO dto) {
        Gato gato=new Gato();
        gato.setId(dto.getId());
        gato.setNombre(dto.getNombre());
        gato.setSrcFoto(dto.getFotos());
        gato.setEdad(dto.getEdad());
        gato.setSexo(dto.getSexo());
        gato.setDescripcion(dto.getDescripcion());
        gato.setColor(dto.getColor());
        gato.setTipoPelo(dto.getTipoPelo());
        gato.setFichaVet(this.fichaMap.mapToEntity(dto.getFichaDTO()));
        gato.setVoluntario(this.volMap.mapToEntity(dto.getVoluntario()));
        gato.setPadrino(dto.getPadrino());
        gato.setAdoptadoFecha(dto.getAdoptado());
        return gato;
    }

    @Override
    public GatoDTO mapToDto(Gato entity) {
        GatoDTO dto=new GatoDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setFotos(entity.getSrcFoto());
        dto.setEdad(entity.getEdad());
        dto.setSexo(entity.getSexo());
        dto.setDescripcion(entity.getDescripcion());
        dto.setColor(entity.getColor());
        dto.setTipoPelo(entity.getTipoPelo());
        dto.setFichaDTO(this.fichaMap.mapToDto(entity.getFichaVet()));
        if(entity.getListaSol()!=null){
            dto.setSolicitudes(this.mapSoliToDto(entity.getListaSol()));
        }
        dto.setVoluntario(this.volMap.mapToDto(entity.getVoluntario()));
        dto.setPadrino(entity.getPadrino());
        dto.setAdoptado(entity.getAdoptadoFecha());
        return dto;
    }

    @Override
    public List<GatoDTO> mapListToDto(List<Gato> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SolicitudReqDTO mapToSoliDTO(SolicitudAdopcion soli){
        SolicitudReqDTO dto=new SolicitudReqDTO();
        dto.setId(soli.getId());
        dto.setEstados(this.estadoMapper.mapToListDto(soli.getEstados()));
        PersonaEmailDTO persoEmail=new PersonaEmailDTO();
        persoEmail.setEmail(soli.getSolicitante().getEmail());
        return dto;
    }

    private List<SolicitudReqDTO> mapSoliToDto(List<SolicitudAdopcion> solicitudes){
        return solicitudes.stream()
                .map(this::mapToSoliDTO)
                .collect(Collectors.toList());
    }
}
