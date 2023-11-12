package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.PadrinoDTO;
import com.example.gatosspringboot.model.Persona;

public interface IPadrinoMapper {

    PadrinoDTO mapToDto(Persona persona);
}
