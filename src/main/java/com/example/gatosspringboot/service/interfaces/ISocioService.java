package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Socio;

public interface ISocioService {
    Socio buscarByEmail(String email);
}
