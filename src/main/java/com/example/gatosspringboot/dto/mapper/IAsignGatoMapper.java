package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.AsignacionGatoDTO;
import com.example.gatosspringboot.model.GatoTransito;

import java.util.List;

public interface IAsignGatoMapper {
    AsignacionGatoDTO mapToDto(GatoTransito entity);
    List<AsignacionGatoDTO> mapToListDto(List<GatoTransito> asignacionesGatos);
}
