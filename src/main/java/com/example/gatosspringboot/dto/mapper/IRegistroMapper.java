package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.RegistroDTO;
import com.example.gatosspringboot.model.Persona;

public interface IRegistroMapper {
    Persona mapToEntity(RegistroDTO dto);
}
