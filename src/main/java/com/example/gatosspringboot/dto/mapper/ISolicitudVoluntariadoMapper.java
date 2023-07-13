package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.SolicitudVoluntariadoDTO;
import com.example.gatosspringboot.model.SolicitudVoluntariado;

import java.util.List;

public interface ISolicitudVoluntariadoMapper {
    SolicitudVoluntariado mapToEntity(SolicitudVoluntariadoDTO dto);
    SolicitudVoluntariadoDTO mapToDto(SolicitudVoluntariado entity);
    List<SolicitudVoluntariadoDTO> mapToListDto(List<SolicitudVoluntariado> entities);
}
