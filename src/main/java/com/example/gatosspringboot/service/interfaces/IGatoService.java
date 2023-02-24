package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Gato;

import java.util.List;

public interface IGatoService {

    List<Gato> verTodos();
    Gato altaGato(Gato gato);
    Gato modiGato(Gato gato, Long id);
    boolean existeGato(Long id);
}
