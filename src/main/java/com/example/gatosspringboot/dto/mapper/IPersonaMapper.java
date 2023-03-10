package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.PersonaDTO;
import com.example.gatosspringboot.model.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface IPersonaMapper {

    @Mapping(source="solicitudes", target="listaSol")
    Persona mapToEntity(PersonaDTO dto);

    @Mapping(source="listaSol", target="solicitudes")
    PersonaDTO mapToDto(Persona entity);

    List<PersonaDTO> mapListToDto(List<Persona> personas);
}
