package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.SolicitudReqDTO;
import com.example.gatosspringboot.dto.SolicitudRespDTO;
import com.example.gatosspringboot.dto.mapper.ISolicitudMapper;
import com.example.gatosspringboot.dto.validator.PostValidationGroup;
import com.example.gatosspringboot.dto.validator.PutValidationGroup;
import com.example.gatosspringboot.model.Solicitud;
import com.example.gatosspringboot.service.interfaces.ISolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",data);
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
    private ResponseEntity<?> nuevaSolicitud(@RequestBody @Validated(PostValidationGroup.class)
                                                 SolicitudReqDTO dto){
        Solicitud nueva=this.mapper.mapToEntity(dto);
        Solicitud creada=this.service.altaSolicitud(nueva);
        SolicitudRespDTO dtoCreado=this.mapper.mapToDto(creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreado);
    }

    @GetMapping
    private ResponseEntity<?> listar(){
        List<Solicitud> solicitudes=this.service.verSolicitudes();
        List<SolicitudRespDTO> dtos=this.mapper.mapListToDto(solicitudes);
        return this.successResponse(dtos);
    }

    @GetMapping("/estado/{estado}")
    private ResponseEntity<?> listarByEstado(@PathVariable String estado){
        List<Solicitud> solicitudes=this.service.verByEstado(estado);
        List<SolicitudRespDTO> dtos=this.mapper.mapListToDto(solicitudes);
        return this.successResponse(dtos);
    }

    @GetMapping("/gato/{id}")
    private ResponseEntity<?> listarByGato(@PathVariable Long id){
        List<Solicitud> solicitudes=this.service.verByGato(id);
        List<SolicitudRespDTO> dtos=this.mapper.mapListToDto(solicitudes);
        return this.successResponse(dtos);
    }

    @GetMapping("/solicitante/{dni}")
    private ResponseEntity<?> listarBySoli(@PathVariable String dni){
        List<Solicitud> solicitudes=this.service.verBySolicitante(dni);
        List<SolicitudRespDTO> dtos=this.mapper.mapListToDto(solicitudes);
        return this.successResponse(dtos);
    }

    @PutMapping("/{id}/estados/aceptada")
    private ResponseEntity<?> aceptarAdopcion(@PathVariable Long id, @RequestBody @Validated(PutValidationGroup.class)
                                              SolicitudReqDTO dto){
        String motivo= dto.getMotivo();
        Solicitud actualizada=this.service.aceptarAdopcion(id,motivo);
        return this.successResponse(this.mapper.mapToDto(actualizada));
    }

}
