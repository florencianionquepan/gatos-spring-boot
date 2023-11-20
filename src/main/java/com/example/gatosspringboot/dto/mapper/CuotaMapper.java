package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.CuotaDTO;
import com.example.gatosspringboot.dto.CuotaRespDTO;
import com.example.gatosspringboot.dto.GatoDTO;
import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.model.Persona;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CuotaMapper implements ICuotaMapper{

    private final IPadrinoMapper padriMap;

    public CuotaMapper(IPadrinoMapper padriMap) {
        this.padriMap = padriMap;
    }

    @Override
    public Cuota mapToEntity(CuotaDTO dto) {
        Cuota cuota=new Cuota();
        //seteo padrino por email
        Persona persona=new Persona();
        persona.setEmail(dto.getPadrino().getEmail());
        Padrino padrino=new Padrino();
        padrino.setPersona(persona);
        cuota.setPadrino(padrino);
        //seteo gato por id
        Gato gato=new Gato();
        gato.setId(dto.getGato().getId());
        cuota.setGato(gato);
        cuota.setMontoMensual(dto.getGato().getMontoMensual());
        return cuota;
    }

    @Override
    public CuotaRespDTO mapToDto(Cuota entity) {
        CuotaRespDTO dto=new CuotaRespDTO();
        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaPago(entity.getFechaPago());
        dto.setMontoMensual(entity.getMontoMensual());
        dto.setPreferencia_id(entity.getPreferencia_id());
        GatoDTO gatodto=new GatoDTO(entity.getGato().getId(),entity.getGato().getAdoptadoFecha(),entity.getGato().getNombre());
        if(entity.getGato().getFotos().size()>0){
            List<String> urls = entity.getGato().getFotos()
                    .stream()
                    .map(foto -> foto.getFotoUrl())
                    .collect(Collectors.toList());
            gatodto.setFotos(urls);
        }
        dto.setGato(gatodto);
        dto.setEstadoPago(entity.getEstadoPago());
        Persona perso=entity.getPadrino().getPersona();
        dto.setPadrino(this.padriMap.mapToDto(perso));
        return dto;
    }

    @Override
    public List<CuotaRespDTO> mapToListDto(List<Cuota> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
