package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Socio;

import java.util.List;

public interface ISocioService {
    Socio buscarByEmail(String email);
    Socio altaSocio(Socio socio);
    List<Socio> listarTodos();
}
