package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.mapper.IAsignGatoMapper;
import com.example.gatosspringboot.dto.mapper.IGatoMapper;
import com.example.gatosspringboot.dto.mapper.ITransitoMapper;
import com.example.gatosspringboot.model.GatoTransito;
import com.example.gatosspringboot.model.Transito;
import com.example.gatosspringboot.service.interfaces.ITransitoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transitos")
public class TransitoController {

    private final ITransitoService service;
    private final ITransitoMapper mapper;
    private final IGatoMapper gatoMapper;
    private final IAsignGatoMapper asignGatoMapper;
    public Map<String,Object> mensajeBody= new HashMap<>();

    public TransitoController(ITransitoService service,
                              ITransitoMapper mapper,
                              IGatoMapper gatoMapper,
                              IAsignGatoMapper asignGatoMapper) {
        this.service = service;
        this.mapper = mapper;
        this.gatoMapper = gatoMapper;
        this.asignGatoMapper = asignGatoMapper;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    @GetMapping
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> listarTodos(){
        List<Transito> transitos=this.service.listarTodos();
        return this.successResponse(this.mapper.mapToListDto(transitos));
    }

    @GetMapping("/localidad/{localidad}")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> listarByLocalidad(@PathVariable String localidad){
        List<Transito> transitos=this.service.listarByLocalidad(localidad);
        return this.successResponse(this.mapper.mapToListDto(transitos));
    }

    @GetMapping("/{email}/gatos")
    @PreAuthorize("#email==authentication.principal")
    public ResponseEntity<?> verGatos(@PathVariable String email){
        List<GatoTransito> gatos=this.service.listarAsignacionesGatos(email);
        return this.successResponse(this.asignGatoMapper.mapToListDto(gatos));
    }

}
