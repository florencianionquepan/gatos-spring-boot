package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.SolicitudDTO;
import com.example.gatosspringboot.model.Solicitud;

import java.util.List;

public interface ISolicitudMapper {
    Solicitud mapToEntity(SolicitudDTO dto);
    SolicitudDTO mapToDto(Solicitud entity);
    List<SolicitudDTO> mapListToDto(List<Solicitud> entities);
}
