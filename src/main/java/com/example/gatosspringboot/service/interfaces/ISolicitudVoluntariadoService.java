package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.SolicitudVoluntariado;

import java.util.List;

public interface ISolicitudVoluntariadoService {
    SolicitudVoluntariado nueva(SolicitudVoluntariado solicitud);
    List<SolicitudVoluntariado> listarByEstado();
    List<SolicitudVoluntariado> listarByPersona(String dni);
    List<SolicitudVoluntariado> listarTodas();
}
