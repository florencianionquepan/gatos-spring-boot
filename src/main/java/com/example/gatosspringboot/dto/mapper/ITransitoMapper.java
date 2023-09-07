package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.TransitoIdDTO;
import com.example.gatosspringboot.dto.TransitoRespDTO;
import com.example.gatosspringboot.model.Transito;

import java.util.List;

public interface ITransitoMapper {

    TransitoRespDTO mapToDto(Transito entity);
    Transito mapToEntity(TransitoIdDTO dto);
    List<TransitoRespDTO> mapToListDto(List<Transito> entities);
}
