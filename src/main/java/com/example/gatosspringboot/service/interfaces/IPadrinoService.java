package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;

public interface IPadrinoService {
    Padrino buscarByEmailOrException(String email);
    void notificarAdopcion(Padrino padrino, Gato gato);
}
