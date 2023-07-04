package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.PersonaDTO;
import com.example.gatosspringboot.model.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface IPersonaMapper {
    Persona mapToPersona(PersonaDTO dto);
    PersonaDTO mapToDto(Persona entity);
    List<Persona> mapToListDto(List<Persona> entities);
}
