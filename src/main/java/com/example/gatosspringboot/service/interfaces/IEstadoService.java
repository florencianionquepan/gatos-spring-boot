package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Estado;

public interface IEstadoService {
    Estado estadoPendiente(Estado estado);
    Estado estadoAprobado(Estado estado, Long id);
    Estado estadoRechazado(Estado estado, Long id);
}
