package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.VoluntarioDTO;
import com.example.gatosspringboot.model.Voluntario;

import java.util.List;

public interface IVoluntarioMapper {

    Voluntario mapToEntity(VoluntarioDTO volu);
    VoluntarioDTO mapToDto(Voluntario entity);
    List<VoluntarioDTO> mapToListDto(List<Voluntario> entities);
}
