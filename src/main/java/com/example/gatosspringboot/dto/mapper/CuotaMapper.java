package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.CuotaDTO;
import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class CuotaMapper implements ICuotaMapper{
    @Override
    public Cuota mapToEntity(CuotaDTO dto) {
        Cuota cuota=new Cuota();
        //seteo padrino por email
        Persona persona=new Persona();
        persona.setEmail(dto.getPadrino().getEmail());
        Padrino padrino=new Padrino();
        padrino.setPersona(persona);
        cuota.setPadrino(padrino);
        //seteo gato por id
        Gato gato=new Gato();
        gato.setId(dto.getGato().getId());
        cuota.setGato(gato);
        cuota.setMontoMensual(dto.getGato().getMontoMensual());
        return cuota;
    }
}
