package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Notificacion;

import java.util.List;

public interface INotificacionService {
    Notificacion nuevaSolicitudAdopcion(Gato gato);
    List<Notificacion> verByPersona(String email);
    List<Notificacion> setearComoLeidas(List<Notificacion> notificaciones);
}
