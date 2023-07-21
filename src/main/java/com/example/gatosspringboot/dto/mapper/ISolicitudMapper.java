package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.SolicitudReqDTO;
import com.example.gatosspringboot.dto.SolicitudRespDTO;
import com.example.gatosspringboot.model.Solicitud;

import java.util.List;

public interface ISolicitudMapper {
    Solicitud mapToEntity(SolicitudReqDTO dto);
    SolicitudRespDTO mapToDto(Solicitud entity);
    List<SolicitudRespDTO> mapListToDto(List<Solicitud> entities);
}
