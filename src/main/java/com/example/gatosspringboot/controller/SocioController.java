package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.SocioDTO;
import com.example.gatosspringboot.dto.mapper.ISocioMapper;
import com.example.gatosspringboot.model.Socio;
import com.example.gatosspringboot.service.interfaces.ISocioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/socios")
public class SocioController {

    private final ISocioService service;
    private final ISocioMapper mapper;

    public Map<String,Object> mensajeBody= new HashMap<>();

    public SocioController(ISocioService service,
                           ISocioMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    private ResponseEntity<?> notSuccessResponse(String mensaje,Integer id){
        mensajeBody.put("Success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }

    @GetMapping
    @PreAuthorize("hasRole('SOCIO')")
    public ResponseEntity<?> listarTodos(){
        List<Socio> socios=this.service.listarTodos();
        List<SocioDTO> dtos=this.mapper.mapToListDto(socios);
        return this.successResponse(dtos);
    }

    @PostMapping
    @PreAuthorize("hasRole('SOCIO')")
    public ResponseEntity<?> altaSocio(@RequestBody @Valid SocioDTO dto){
        Socio nuevo=this.service.altaSocio(this.mapper.mapToEntity(dto));
        return this.successResponse(this.mapper.mapToDto(nuevo));
    }

}
