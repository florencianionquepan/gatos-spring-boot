package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.SolicitudVoluntariado;
import com.example.gatosspringboot.service.interfaces.ISolicitudVoluntariadoService;

import java.util.List;

public class SolicitudVoluntariadoService implements ISolicitudVoluntariadoService {
    @Override
    public SolicitudVoluntariado nueva(SolicitudVoluntariado solicitud) {
        return null;
    }

    @Override
    public List<SolicitudVoluntariado> listarByEstado() {
        return null;
    }

    @Override
    public List<SolicitudVoluntariado> listarByPersona(String dni) {
        return null;
    }

    @Override
    public List<SolicitudVoluntariado> listarTodas() {
        return null;
    }
}
