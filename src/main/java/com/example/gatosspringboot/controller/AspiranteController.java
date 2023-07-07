package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.AspiranteDTO;
import com.example.gatosspringboot.service.interfaces.IAspiranteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aspirantes")
public class AspiranteController {

    private final IAspiranteService service;
    public Map<String,Object> mensajeBody= new HashMap<>();

    public AspiranteController(IAspiranteService service) {
        this.service = service;
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

    @PostMapping
    public ResponseEntity<?> nuevo(@RequestBody @Valid AspiranteDTO dto){
        return null;
    }
}
