package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.GatoIdDTO;
import com.example.gatosspringboot.model.Gato;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface IGatoIdMapper {
    Gato mapToEntity(GatoIdDTO dto);
    GatoIdDTO mapToDto(Gato entidad);
}
