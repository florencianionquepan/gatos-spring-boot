package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.TransitoIdDTO;
import com.example.gatosspringboot.model.Transito;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ITransitoIdMapper {
    Transito mapToEntity(TransitoIdDTO dto);
}
