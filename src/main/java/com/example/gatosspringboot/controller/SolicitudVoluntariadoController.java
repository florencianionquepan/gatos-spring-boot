package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.service.interfaces.ISolicitudVoluntariadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/voluntariados")
public class SolicitudVoluntariadoController {

    private final ISolicitudVoluntariadoService service;

    public SolicitudVoluntariadoController(ISolicitudVoluntariadoService service) {
        this.service = service;
    }

    public Map<String,Object> mensajeBody= new HashMap<>();

    private ResponseEntity<?> successResponse(List<?> lista){
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",lista);
        return ResponseEntity.ok(mensajeBody);
    }

    private ResponseEntity<?> notSuccessResponse(String mensaje,int id){
        mensajeBody.put("Success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }
}
