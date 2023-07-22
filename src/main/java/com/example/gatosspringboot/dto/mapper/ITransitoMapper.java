package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.TransitoRespDTO;
import com.example.gatosspringboot.model.Transito;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface ITransitoMapper {

    TransitoRespDTO mapToDto(Transito entity);
    Transito mapToEntity(TransitoRespDTO dto);
    List<TransitoRespDTO> mapToListDto(List<Transito> entities);
}
