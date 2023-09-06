package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Voluntario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
//lo uso para mostrar datos de voluntario
//el voluntario se crea cuando acepta solicitud voluntariado
public class VoluntarioMapper implements IVoluntarioMapper{

    private final GatoMapper gatoMapper;

    public VoluntarioMapper(GatoMapper gatoMapper) {
        this.gatoMapper = gatoMapper;
    }

    @Override
    //esto no se usa igualmente.
    public Voluntario mapToEntity(VoluntarioDTO volu) {
        Voluntario ent=new Voluntario();
        //no se ingresan gatos al hacer post, para agregar ver
        return ent;
    }

    @Override
    public VoluntarioDTO mapToDto(Voluntario entity) {
        VoluntarioDTO dto=new VoluntarioDTO();
        Persona perso=entity.getPersona();
        dto.setId(entity.getId());
        dto.setDni(perso.getDni());
        dto.setNombre(perso.getNombre());
        dto.setApellido(perso.getApellido());
        dto.setTel(perso.getTel());
        dto.setEmail(perso.getEmail());
        dto.setFechaNac(perso.getFechaNac());
        dto.setDire(perso.getDire());
        dto.setLocalidad(perso.getLocalidad());
        dto.setGatos(this.gatoMapper.mapListToDto(entity.getListaGatos()));
        return dto;
    }

    @Override
    public List<VoluntarioDTO> mapToListDto(List<Voluntario> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
