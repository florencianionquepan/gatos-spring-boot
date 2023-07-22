package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Solicitud;

public interface IPersonaService {
    Persona findByDni(String dni);
    Persona findByEmailOrException(String email);
    boolean existeByDni(String dni);
    void validarEmailUnico(String email);
    boolean validarEmailIngresado(String email);
    Persona altaRegistro(Persona persona, String token);
}
