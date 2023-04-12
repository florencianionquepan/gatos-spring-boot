package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioDTO;
import com.example.gatosspringboot.dto.VoluntarioUsuarioDTO;
import com.example.gatosspringboot.model.Voluntario;

public interface IVoluntUsuarioMapper {
    Voluntario mapToEntity(VoluntarioUsuarioDTO dto);
    VoluntarioDTO mapToDto(Voluntario entity);
}
