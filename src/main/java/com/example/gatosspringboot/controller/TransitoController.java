package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.mapper.ITransitoMapper;
import com.example.gatosspringboot.model.Transito;
import com.example.gatosspringboot.service.interfaces.ITransitoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transitos")
public class TransitoController {

    private final ITransitoService service;
    private final ITransitoMapper mapper;
    public Map<String,Object> mensajeBody= new HashMap<>();

    public TransitoController(ITransitoService service,
                              ITransitoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

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

    @GetMapping
    public ResponseEntity<?> listarTodos(){
        List<Transito> transitos=this.service.listarTodos();
        return this.successResponse(this.mapper.mapToListDto(transitos));
    }

    @GetMapping("/localidad/{localidad}")
    public ResponseEntity<?> listarByLocalidad(@PathVariable String localidad){
        List<Transito> transitos=this.service.listarByLocalidad(localidad);
        return this.successResponse(this.mapper.mapToListDto(transitos));
    }

}
