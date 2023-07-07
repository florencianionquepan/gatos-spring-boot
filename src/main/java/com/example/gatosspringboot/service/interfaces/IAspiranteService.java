package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Aspirante;
import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.Socio;

import java.util.List;

public interface IAspiranteService {
    Aspirante altaAspirante(Aspirante aspirante);
    Aspirante aceptarAspirante(Aspirante aspirante);
    Aspirante rechazarAspirante(Aspirante aspirante);
    List<Aspirante> listarTodos();
    List<Aspirante> listarByEstado(Estado estado);
    List<Aspirante> listarBySocio(Socio socio);
}
