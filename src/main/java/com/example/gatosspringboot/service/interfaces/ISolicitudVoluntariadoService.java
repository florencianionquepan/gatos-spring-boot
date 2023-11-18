package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.SolicitudVoluntariado;

import java.util.List;

public interface ISolicitudVoluntariadoService {
    SolicitudVoluntariado nueva(SolicitudVoluntariado solicitud);
    List<SolicitudVoluntariado> listarByPersona(String email);
    SolicitudVoluntariado rechazar(SolicitudVoluntariado solicitud, Long id, String motivo);
    SolicitudVoluntariado aceptar(SolicitudVoluntariado solicitud, Long id, String motivo);
    List<SolicitudVoluntariado> listarByEstado(String estado);
    List<SolicitudVoluntariado> listarTodas();
}
