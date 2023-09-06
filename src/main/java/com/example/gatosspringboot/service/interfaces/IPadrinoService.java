package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Padrino;

public interface IPadrinoService {
    Padrino buscarByEmailOrException(String email);
}
