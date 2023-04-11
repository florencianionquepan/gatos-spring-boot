package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Solicitud;

import java.util.List;

public interface ISolicitudService {
    List<Solicitud> verSolicitudes();
    Solicitud altaSolicitud(Solicitud solicitud);
    Solicitud aceptarAdopcion(Solicitud solicitud, Long id);
    Solicitud rechazarSolicitud(Solicitud solicitud, Long id);
}
