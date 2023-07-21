package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.PersonaEmailDTO;
import com.example.gatosspringboot.model.Persona;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface IPersonaEmailMapper {
    Persona mapToEntity(PersonaEmailDTO dto);
}
