package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.*;

import java.util.List;

public interface INotificacionService {
    Notificacion nuevaSolicitudAdopcion(Gato gato);
    Notificacion asignacionTransito(Gato gato, Transito transito);
    Notificacion asignacionTransito(Gato gato, Padrino padrino);
    Notificacion rechazoAdopcion(Gato gato, Persona solicitante);
    Notificacion aprobacionAdopcion(Gato gato, Persona solicitante);
    List<Notificacion> verByPersona(String email);
    List<Notificacion> setearComoLeidas(List<Notificacion> notificaciones);
}
