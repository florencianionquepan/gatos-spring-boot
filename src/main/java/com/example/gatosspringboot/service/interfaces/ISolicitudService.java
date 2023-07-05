package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Solicitud;

import java.util.List;

public interface ISolicitudService {
    List<Solicitud> verSolicitudes();
    List<Solicitud> verByEstado(String estado);
    List<Solicitud> verByGato(Long idGato);
    List<Solicitud> verBySolicitante(String dni);
    Solicitud altaSolicitud(Solicitud solicitud);
    Solicitud aceptarAdopcion(Solicitud solicitud, Long id);
    Solicitud rechazarSolicitud(Solicitud solicitud, Long id);
}
