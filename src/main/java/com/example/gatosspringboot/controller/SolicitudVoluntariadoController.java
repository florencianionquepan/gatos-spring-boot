package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.SolicitudVoluntariadoDTO;
import com.example.gatosspringboot.dto.mapper.ISolicitudVoluntariadoMapper;
import com.example.gatosspringboot.dto.validator.PostValidationGroup;
import com.example.gatosspringboot.dto.validator.PutValidationGroup;
import com.example.gatosspringboot.model.SolicitudVoluntariado;
import com.example.gatosspringboot.service.imple.SolicitudVoluntariadoService;
import com.example.gatosspringboot.service.interfaces.ISolicitudVoluntariadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/voluntariados")
public class SolicitudVoluntariadoController {

    private final ISolicitudVoluntariadoService service;
    private final ISolicitudVoluntariadoMapper mapper;
    private Logger logger= LoggerFactory.getLogger(SolicitudVoluntariadoController.class);

    public SolicitudVoluntariadoController(ISolicitudVoluntariadoService service,
                                           ISolicitudVoluntariadoMapper mapper) {
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
    public ResponseEntity<?> nueva(@RequestBody @Validated(PostValidationGroup.class)
                                   SolicitudVoluntariadoDTO dto){
        SolicitudVoluntariado creada=this.service.nueva(this.mapper.mapToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.mapToDto(creada));
    }

    @PutMapping("/{id}/estados/aceptada")
    public ResponseEntity<?> aceptarSolicitud(@RequestBody @Validated(PutValidationGroup.class)
                                                  SolicitudVoluntariadoDTO dto, @PathVariable Long id){
        SolicitudVoluntariado aceptada=this.service.aceptar(this.mapper.mapToEntityForPut(dto),id);
        return this.successResponse(this.mapper.mapToDto(aceptada));
    }

    @PutMapping("/{id}/estados/rechazada")
    public ResponseEntity<?> rechazarSolicitud(@RequestBody @Validated(PutValidationGroup.class)
                                               SolicitudVoluntariadoDTO dto, @PathVariable Long id){
        String motivo= dto.getMotivo();
        SolicitudVoluntariado rechazada=this.service.rechazar(this.mapper.mapToEntityForPut(dto),id,motivo);
        return this.successResponse(this.mapper.mapToDto(rechazada));
    }

}
