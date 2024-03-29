package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.*;

import java.util.List;

public interface INotificacionService {
    Notificacion nuevaSolicitudAdopcion(Gato gato);
    Notificacion asignacionTransito(Gato gato, Transito transito);
    Notificacion asignacionTransito(Gato gato, Padrino padrino);
    Notificacion desasignacionTransito(Gato gato, Transito transitoAnterior);
    Notificacion rechazoAdopcion(Gato gato, Persona solicitante);
    Notificacion aprobacionAdopcion(Gato gato, Persona solicitante);
    Notificacion notificarAdopcion(Transito transito, Gato gato);
    Notificacion notificarAdopcion(Padrino padrino, Gato gato);
    Notificacion cierreAdopcion(Gato gato, Persona solicitante);
    Notificacion actualizacionCuota(Padrino padrino, Gato gato);
    List<Notificacion> verByPersona(String email);
    List<Notificacion> setearComoLeidas(List<Notificacion> notificaciones);
    Notificacion nuevaSolicitudVoluntariado(Persona solicitante, TipoVoluntariado tipo);
    Notificacion rechazoVoluntariado(Persona aspirante, TipoVoluntariado tipo);
    Notificacion aceptarVoluntariado(Persona aspirante, TipoVoluntariado tipo);
    Notificacion nuevoRolSocio(Persona persona);
}
