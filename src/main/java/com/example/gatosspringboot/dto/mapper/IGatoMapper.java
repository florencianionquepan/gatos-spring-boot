package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.GatoDTO;
import com.example.gatosspringboot.dto.GatoRespDTO;
import com.example.gatosspringboot.model.Gato;

import java.util.List;

public interface IGatoMapper {
    Gato mapToEntity(GatoDTO dto);
    GatoRespDTO mapToDto(Gato entity);
    GatoDTO mapToDtoSimple(Gato entity);
    List<GatoRespDTO> mapListToDto(List<Gato> entities);

}
