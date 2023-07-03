package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.FichaDTO;
import com.example.gatosspringboot.model.Ficha;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface IFichaMapper {
    Ficha mapToEntity(FichaDTO dto);
    FichaDTO mapToDto(Ficha entity);
}
