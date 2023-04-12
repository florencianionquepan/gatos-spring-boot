package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.GatoDTO;
import com.example.gatosspringboot.model.Gato;

import java.util.List;

public interface IGatoMapper {
    Gato mapToEntity(GatoDTO dto);
    GatoDTO mapToDto(Gato entity);
    List<GatoDTO> mapListToDto(List<Gato> entities);

}
