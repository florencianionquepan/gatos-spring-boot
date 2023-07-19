package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.TransitoDTO;
import com.example.gatosspringboot.model.Transito;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface ITransitoMapper {

    TransitoDTO mapToDto(Transito entity);
    Transito mapToEntity(TransitoDTO dto);
    List<TransitoDTO> mapToListDto(List<Transito> entities);
}
