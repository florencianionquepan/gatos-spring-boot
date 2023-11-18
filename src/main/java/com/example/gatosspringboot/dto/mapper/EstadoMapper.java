package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.EstadoDTO;
import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.EstadoNombre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstadoMapper implements IEstadoMapper{

    @Override
    public Estado mapToEntity(EstadoDTO dto) {
        Estado estado=new Estado();
        estado.setFecha(dto.getFecha());
        estado.setEstado(EstadoNombre.valueOf(dto.getEstado()));
        estado.setMotivo(dto.getMotivo());
        return estado;
    }

    @Override
    public EstadoDTO mapToDto(Estado entidad) {
        EstadoDTO dto=new EstadoDTO();
        dto.setId(entidad.getId());
        dto.setFecha(entidad.getFecha());
        dto.setEstado(entidad.getEstado().name());
        dto.setMotivo(entidad.getMotivo());
        return dto;
    }

    @Override
    public List<EstadoDTO> mapToListDto(List<Estado> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
