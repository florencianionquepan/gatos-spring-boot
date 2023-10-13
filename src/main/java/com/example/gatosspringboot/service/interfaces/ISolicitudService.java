package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.SolicitudAdopcion;

import java.time.LocalDate;
import java.util.List;

public interface ISolicitudService {
    List<SolicitudAdopcion> verSolicitudes();
    List<SolicitudAdopcion> verByEstado(String estado);
    List<SolicitudAdopcion> verByGato(Long idGato);
    List<SolicitudAdopcion> verBySolicitante(String dni);
    List<SolicitudAdopcion> verRangoFechas(LocalDate desde, LocalDate hasta);
    List<SolicitudAdopcion> verByGatoPendientes(Long idGato);
    SolicitudAdopcion altaSolicitud(SolicitudAdopcion solicitudAdopcion);
    SolicitudAdopcion aceptarAdopcion(Long id, String motivo);
    SolicitudAdopcion rechazarSolicitud(Long id, String motivo);
}
