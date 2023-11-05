package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.GatoTransito;
import com.example.gatosspringboot.model.Transito;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface ITransitoService {
    List<Transito> listarTodos();
    List<Transito> listarByLocalidad(String localidad);
    Transito nuevo(Transito transito);
    Transito findByIdOrException(Long id);
    Transito addGato(GatoTransito asociacion);
    Transito notificarTransitoAnterior(Gato gato, Transito anterior);
    HashMap<LocalDate,Gato> listarAsignacionesGatos(String email);
    void notificarAdopcion(Transito transito, Gato gato);
}
