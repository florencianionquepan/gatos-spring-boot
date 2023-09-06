package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.SolicitudReqDTO;
import com.example.gatosspringboot.dto.SolicitudRespDTO;
import com.example.gatosspringboot.model.SolicitudAdopcion;

import java.util.List;

public interface ISolicitudMapper {
    SolicitudAdopcion mapToEntity(SolicitudReqDTO dto);
    SolicitudRespDTO mapToDto(SolicitudAdopcion entity);
    List<SolicitudRespDTO> mapListToDto(List<SolicitudAdopcion> entities);
}
