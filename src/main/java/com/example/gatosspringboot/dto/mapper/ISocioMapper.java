package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.SocioDTO;
import com.example.gatosspringboot.model.Socio;
import java.util.List;

public interface ISocioMapper {
    SocioDTO mapToDto(Socio entity);
    Socio mapToEntity(SocioDTO dto);
    List<SocioDTO> mapToListDto(List<Socio> entities);
}
