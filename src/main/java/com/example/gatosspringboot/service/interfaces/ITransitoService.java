package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Transito;

import java.util.List;

public interface ITransitoService {
    List<Transito> listarTodos();
    List<Transito> listarByLocalidad(String localidad);
    Transito nuevo(Transito transito);
    Transito findByIdOrException(Long id);
    Transito addGato(Gato gato, Transito transito);
    List<Gato> listarGatos(String email);
}
