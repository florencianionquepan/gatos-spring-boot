package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.SolicitudDTO;
import com.example.gatosspringboot.dto.mapper.ISolicitudMapper;
import com.example.gatosspringboot.model.Solicitud;
import com.example.gatosspringboot.service.interfaces.ISolicitudService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final ISolicitudService service;
    private final ISolicitudMapper mapper;

    public SolicitudController(ISolicitudService service,
                               ISolicitudMapper mapper) {
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
    private ResponseEntity<?> nuevaSolicitud(@RequestBody @Valid SolicitudDTO dto){
        Solicitud nueva=this.mapper.mapToEntity(dto);
        Solicitud creada=this.service.altaSolicitud(nueva);
        SolicitudDTO dtoCreado=this.mapper.mapToDto(creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreado);
    }

    @GetMapping
    private ResponseEntity<?> listar(){
        List<Solicitud> solicitudes=this.service.verSolicitudes();
        List<SolicitudDTO> dtos=this.mapper.mapListToDto(solicitudes);
        return this.successResponse(dtos);
    }

    @GetMapping("/estado/{estado}")
    private ResponseEntity<?> listarByEstado(@PathVariable String estado){
        List<Solicitud> solicitudes=this.service.verByEstado(estado);
        List<SolicitudDTO> dtos=this.mapper.mapListToDto(solicitudes);
        return this.successResponse(dtos);
    }

}
