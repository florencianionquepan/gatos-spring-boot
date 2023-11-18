package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.SolicitudVoluntariadoDTO;
import com.example.gatosspringboot.dto.mapper.ISolicitudVoluntariadoMapper;
import com.example.gatosspringboot.dto.validator.PostValidationGroup;
import com.example.gatosspringboot.dto.validator.PutValidationGroup;
import com.example.gatosspringboot.model.SolicitudVoluntariado;
import com.example.gatosspringboot.service.interfaces.ISolicitudVoluntariadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private Logger logger= LoggerFactory.getLogger(SolicitudVoluntariadoController.class);

    public SolicitudVoluntariadoController(ISolicitudVoluntariadoService service,
                                           ISolicitudVoluntariadoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    public Map<String,Object> mensajeBody= new HashMap<>();

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    @PostMapping
    public ResponseEntity<?> nueva(@RequestBody @Validated(PostValidationGroup.class)
                                   SolicitudVoluntariadoDTO dto){
        SolicitudVoluntariado creada=this.service.nueva(this.mapper.mapToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.mapToDto(creada));
    }

    @GetMapping("/aspirante/{email}")
    @PreAuthorize("#email==authentication.principal")
    public ResponseEntity<?> verByAspirante(@PathVariable String email){
        List<SolicitudVoluntariado> solicitudes=this.service.listarByPersona(email);
        return this.successResponse(this.mapper.mapToListDto(solicitudes));
    }

    @PutMapping("/{id}/estados/aceptada")
    @PreAuthorize("hasRole('SOCIO')")
    public ResponseEntity<?> aceptarSolicitud(@RequestBody @Validated(PutValidationGroup.class)
                                                  SolicitudVoluntariadoDTO dto, @PathVariable Long id){
        String motivo= dto.getMotivo();
        SolicitudVoluntariado aceptada=this.service.aceptar(this.mapper.mapToEntityForPut(dto),id, motivo);
        return this.successResponse(this.mapper.mapToDto(aceptada));
    }

    @PutMapping("/{id}/estados/rechazada")
    @PreAuthorize("hasRole('SOCIO')")
    public ResponseEntity<?> rechazarSolicitud(@RequestBody @Validated(PutValidationGroup.class)
                                               SolicitudVoluntariadoDTO dto, @PathVariable Long id){
        String motivo= dto.getMotivo();
        SolicitudVoluntariado rechazada=this.service.rechazar(this.mapper.mapToEntityForPut(dto),id,motivo);
        return this.successResponse(this.mapper.mapToDto(rechazada));
    }

    @GetMapping("/estados/{estado}")
    @PreAuthorize("hasRole('SOCIO')")
    public ResponseEntity<?> verByEstado(@PathVariable String estado){
        List<SolicitudVoluntariado> solicitudes=this.service.listarByEstado(estado);
        return this.successResponse(this.mapper.mapToListDto(solicitudes));
    }

    @GetMapping
    @PreAuthorize("hasRole('SOCIO')")
    public ResponseEntity<?> verTodas(){
        List<SolicitudVoluntariado> solicitudes=this.service.listarTodas();
        return this.successResponse(this.mapper.mapToListDto(solicitudes));
    }

}
