package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioEmailDTO;
import com.example.gatosspringboot.model.Voluntario;

public interface IVoluntarioEmailMapper {
    Voluntario mapToEntity(VoluntarioEmailDTO dto);
    VoluntarioEmailDTO mapToDto(Voluntario entity);
}
