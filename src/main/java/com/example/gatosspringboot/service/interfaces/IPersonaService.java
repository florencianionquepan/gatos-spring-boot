package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Solicitud;

public interface IPersonaService {
    Persona findByDni(String dni);
    boolean existeByDni(String dni);
    void addSolicitudPersona(Solicitud solicitud);
    boolean personaExistente(String dni);
}
