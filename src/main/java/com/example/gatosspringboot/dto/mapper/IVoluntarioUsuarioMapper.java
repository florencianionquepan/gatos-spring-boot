package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioUsuarioDTO;
import com.example.gatosspringboot.model.Voluntario;

public interface IVoluntarioUsuarioMapper {
    Voluntario mapToEntity(VoluntarioUsuarioDTO dto);
    VoluntarioUsuarioDTO mapToDto(Voluntario entity);
}
