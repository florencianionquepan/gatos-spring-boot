package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Estado;

public interface IEstadoService {
    Estado crearPendiente();
    Estado crearAprobado(String motivo);
    Estado crearRechazado(String motivo);
    Estado crearCerrado();
}
