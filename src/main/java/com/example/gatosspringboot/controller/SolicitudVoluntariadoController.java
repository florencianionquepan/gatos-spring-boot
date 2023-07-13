package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.SolicitudVoluntariadoDTO;
import com.example.gatosspringboot.dto.mapper.ISolicitudVoluntariadoMapper;
import com.example.gatosspringboot.dto.validator.PostValidationGroup;
import com.example.gatosspringboot.model.SolicitudVoluntariado;
import com.example.gatosspringboot.service.interfaces.ISolicitudVoluntariadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/voluntariados")
public class SolicitudVoluntariadoController {

    private final ISolicitudVoluntariadoService service;
    private final ISolicitudVoluntariadoMapper mapper;

    public SolicitudVoluntariadoController(ISolicitudVoluntariadoService service,
                                           ISolicitudVoluntariadoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
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

    @PostMapping
    public ResponseEntity<?> nueva(@RequestBody @Validated(PostValidationGroup.class)
                                   SolicitudVoluntariadoDTO dto){
        SolicitudVoluntariado creada=this.service.nueva(this.mapper.mapToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.mapToDto(creada));
    }

    @PutMapping("/{id}/estados/aceptada")
    public ResponseEntity<?> aceptarSolicitud(){
        return null;
    }

    @PutMapping("/{id}/estados/rechazada")
    public ResponseEntity<?> rechazarSolicitud(){
        return null;
    }

}
