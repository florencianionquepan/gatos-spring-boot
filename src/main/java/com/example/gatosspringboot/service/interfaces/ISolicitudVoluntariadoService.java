package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.SolicitudVoluntariado;

import java.util.List;

public interface ISolicitudVoluntariadoService {
    SolicitudVoluntariado nueva(SolicitudVoluntariado solicitud);
    SolicitudVoluntariado rechazar(SolicitudVoluntariado solicitud, Long id, String motivo);
    SolicitudVoluntariado aceptar(SolicitudVoluntariado solicitud, Long id);
    List<SolicitudVoluntariado> listarByEstado(String estado);
    List<SolicitudVoluntariado> listarByPersona(String dni);
    List<SolicitudVoluntariado> listarTodas();
}
