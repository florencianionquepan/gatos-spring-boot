package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.PadrinoDTO;
import com.example.gatosspringboot.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class PadrinoMapper implements IPadrinoMapper{

    @Override
    public PadrinoDTO mapToDto(Persona persona) {
        PadrinoDTO dtoPad=new PadrinoDTO();
        dtoPad.setNombre(persona.getNombre());
        dtoPad.setApellido(persona.getApellido());
        dtoPad.setDni(persona.getDni());
        dtoPad.setFechaNac(persona.getFechaNac());
        dtoPad.setTel(persona.getTel());
        dtoPad.setDire(persona.getDire());
        dtoPad.setLocalidad(persona.getLocalidad());
        dtoPad.setEmail(persona.getEmail());
        return dtoPad;
    }
}
