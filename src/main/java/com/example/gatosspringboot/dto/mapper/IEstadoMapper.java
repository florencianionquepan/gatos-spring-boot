package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.EstadoDTO;
import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.EstadoNombre;

import java.util.List;

public interface IEstadoMapper {
    Estado mapToEntity(EstadoDTO dto);
    EstadoDTO mapToDto(Estado dto);
    List<EstadoDTO> mapToListDto(List<Estado> entities);
}
