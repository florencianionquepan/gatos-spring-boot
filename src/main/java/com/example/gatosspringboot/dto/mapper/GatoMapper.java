package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.*;
import com.example.gatosspringboot.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GatoMapper implements IGatoMapper{

    private final IVoluntarioEmailMapper volMap;
    private final IVoluntarioMapper voluMapper;
    private final ITransitoMapper transitoMap;
    private final IFichaMapper fichaMap;
    private final IEstadoMapper estadoMapper;

    public GatoMapper(IVoluntarioEmailMapper volMap,
                      IVoluntarioMapper voluMapper,
                      ITransitoMapper transitoMap,
                      IFichaMapper fichaMap,
                      IEstadoMapper estadoMapper) {
        this.volMap = volMap;
        this.voluMapper = voluMapper;
        this.transitoMap = transitoMap;
        this.fichaMap = fichaMap;
        this.estadoMapper = estadoMapper;
    }


    @Override
    public Gato mapToEntity(GatoDTO dto) {
        Gato gato=new Gato();
        gato.setId(dto.getId());
        gato.setNombre(dto.getNombre());
        gato.setEdad(dto.getEdad());
        gato.setSexo(dto.getSexo());
        gato.setDescripcion(dto.getDescripcion());
        gato.setColor(dto.getColor());
        gato.setTipoPelo(dto.getTipoPelo());
        if(dto.getMontoMensual()!=null){
            gato.setMontoMensual(dto.getMontoMensual());
        }
        gato.setFichaVet(this.fichaMap.mapToEntity(dto.getFichaDTO()));
        gato.setVoluntario(this.volMap.mapToEntity(dto.getVoluntario()));
        gato.setPadrino(dto.getPadrino());
        gato.setAdoptadoFecha(dto.getAdoptado());
        List<Foto> fotos = new ArrayList<>();
        if(dto.getFotos()!=null){
            for (String url : dto.getFotos()) {
                Foto foto = new Foto();
                foto.setFotoUrl(url);
                fotos.add(foto);
            }
        }
        gato.setFotos(fotos);
        return gato;
    }

    @Override
    public GatoRespDTO mapToDto(Gato entity) {
        GatoRespDTO dto=new GatoRespDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        if(entity.getFotos().size()>0){
            List<String> urls = entity.getFotos()
                    .stream()
                    .map(foto -> foto.getFotoUrl())
                    .collect(Collectors.toList());
            dto.setFotos(urls);
        }
        dto.setEdad(entity.getEdad());
        dto.setSexo(entity.getSexo());
        dto.setDescripcion(entity.getDescripcion());
        dto.setColor(entity.getColor());
        dto.setTipoPelo(entity.getTipoPelo());
        dto.setMontoMensual(entity.getMontoMensual());
        dto.setFicha(this.fichaMap.mapToDto(entity.getFichaVet()));
        if(entity.getSolicitudesAdopcion()!=null){
            dto.setSolicitudes(this.mapSoliToDto(entity.getSolicitudesAdopcion()));
        }
        if(entity.getPadrino()!=null){
            //luego ver que otros datos necesito...
            Persona perso=entity.getPadrino().getPersona();
            PadrinoDTO dtoPad=new PadrinoDTO();
            dtoPad.setId(entity.getPadrino().getId());
            dtoPad.setNombre(perso.getNombre());
            dtoPad.setDni(perso.getDni());
            dto.setPadrino(dtoPad);
        }
        //aca le paso el ultimo
        if(entity.getAsignacionesTransitos()!=null && !entity.getAsignacionesTransitos().isEmpty()){
            List<Transito> transitos=entity.getAsignacionesTransitos().stream()
                    .map(GatoTransito::getTransito)
                    .collect(Collectors.toList());
            Transito ultimo=transitos.get(transitos.size()-1);
            dto.setTransito(this.transitoMap.mapToDto(ultimo));
        }
        dto.setVoluntario(this.voluMapper.mapToDto(entity.getVoluntario()));
        dto.setAdoptado(entity.getAdoptadoFecha());
        return dto;
    }

    @Override
    public GatoDTO mapToDtoSimple(Gato entity) {
        GatoDTO dto=new GatoDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        if(entity.getFotos().size()>0){
            List<String> urls = entity.getFotos()
                    .stream()
                    .map(foto -> foto.getFotoUrl())
                    .collect(Collectors.toList());
            dto.setFotos(urls);
        }
        dto.setEdad(entity.getEdad());
        dto.setSexo(entity.getSexo());
        dto.setDescripcion(entity.getDescripcion());
        dto.setColor(entity.getColor());
        dto.setTipoPelo(entity.getTipoPelo());
        dto.setMontoMensual(entity.getMontoMensual());
        dto.setFichaDTO(this.fichaMap.mapToDto(entity.getFichaVet()));
        if(entity.getSolicitudesAdopcion()!=null){
            dto.setSolicitudes(this.mapSoliToDto(entity.getSolicitudesAdopcion()));
        }
        if(entity.getPadrino()!=null){
            Padrino dtoPad=new Padrino();
            Persona perso=new Persona();
            perso.setNombre(entity.getPadrino().getPersona().getNombre());
            perso.setDni(entity.getPadrino().getPersona().getDni());
            dtoPad.setPersona(perso);
            dto.setPadrino(dtoPad);
        }
        dto.setAdoptado(entity.getAdoptadoFecha());
        return dto;
    }

    @Override
    public List<GatoRespDTO> mapListToDto(List<Gato> entities) {
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
