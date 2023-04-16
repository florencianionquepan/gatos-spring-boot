package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.GatoDTO;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Voluntario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatoMapper implements IGatoMapper{

    private final IVoluntarioMapper volMap;

    public GatoMapper(IVoluntarioMapper volMap) {
        this.volMap = volMap;
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
        gato.setFichaVet(dto.getFichaVet());
        gato.setListaSol(dto.getSolicitudes());
        //Voluntario volEntity=volMap.mapToEntity(dto.getVoluntario());
        //gato.setVoluntario(volEntity);
        gato.setPadrino(dto.getPadrino());
        gato.setAdoptadoFecha(dto.getAdoptado());
        return gato;
    }

    @Override
    public GatoDTO mapToDto(Gato entity) {
        return null;
    }

    @Override
    public List<GatoDTO> mapListToDto(List<Gato> entities) {
        return null;
    }
}
