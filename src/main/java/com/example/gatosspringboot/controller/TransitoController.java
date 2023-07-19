package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.TransitoDTO;
import com.example.gatosspringboot.dto.mapper.ITransitoMapper;
import com.example.gatosspringboot.model.Transito;
import com.example.gatosspringboot.service.interfaces.ITransitoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transitos")
public class TransitoController {

    public final ITransitoService service;
    public final ITransitoMapper mapper;
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
    public ResponseEntity<?> listarTodos(@PathVariable String localidad){
        List<Transito> transitos=this.service.listarByLocalidad(localidad);
        return this.successResponse(this.mapper.mapToListDto(transitos));
    }

    @PostMapping
    public ResponseEntity<?> nuevo(@RequestBody TransitoDTO dto){
        Transito nuevo=this.service.nuevo(this.mapper.mapToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.mapToDto(nuevo));
    }


}
