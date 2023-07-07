package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.AspiranteDTO;
import com.example.gatosspringboot.model.Aspirante;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface IAspiranteMapper {
    Aspirante mapToEntity(AspiranteDTO dto);
    AspiranteDTO mapToDto(Aspirante entity);
    List<AspiranteDTO> mapToListDto(List<Entity> entities);
}
