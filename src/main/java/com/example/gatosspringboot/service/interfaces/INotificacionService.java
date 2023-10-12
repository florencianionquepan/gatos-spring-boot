package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Notificacion;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.model.Transito;

import java.util.List;

public interface INotificacionService {
    Notificacion nuevaSolicitudAdopcion(Gato gato);
    Notificacion asignacionTransito(Gato gato, Transito transito);
    Notificacion asignacionTransito(Gato gato, Padrino padrino);
    List<Notificacion> verByPersona(String email);
    List<Notificacion> setearComoLeidas(List<Notificacion> notificaciones);
}
