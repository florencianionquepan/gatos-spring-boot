package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Ficha;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Transito;

import java.util.List;

public interface IGatoService {

    List<Gato> verTodos();
    Gato altaGato(Gato gato);
    Gato modiGato(Gato gato, Long id);
    boolean existeGato(Long id);
    Gato adoptarGato(Long id);
    Gato buscarDisponibleById(Long id);
    Gato agregarFicha(Ficha ficha, Long id);
    Gato agregarTransito(Transito transito, Long id);
}
