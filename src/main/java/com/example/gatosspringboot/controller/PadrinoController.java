package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.GatoIdDTO;
import com.example.gatosspringboot.dto.mapper.IGatoIdMapper;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.service.interfaces.IPadrinoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/padrinos")
public class PadrinoController {

    private final IPadrinoService service;
    private final IGatoIdMapper gatoMapper;

    public Map<String,Object> mensajeBody= new HashMap<>();

    public PadrinoController(IPadrinoService service,
                             IGatoIdMapper gatoMapper) {
        this.service = service;
        this.gatoMapper = gatoMapper;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    @PutMapping("/{email}")
    @PreAuthorize("#email==authentication.principal")
    //este endpoint es para que el propio padrino remueva un gato
    public ResponseEntity<?> removerGato(@PathVariable String email, @RequestBody GatoIdDTO dto){
        Padrino modi=this.service.removerGato(email,this.gatoMapper.mapToEntity(dto));
        return this.successResponse(modi);
    }

    @GetMapping("/{idPadrino}/cuotas")
    //Se remueven las cuotas impagas de un padrino
    public ResponseEntity<?> revisarCuotas(@PathVariable Long idPadrino){
        Padrino modi=this.service.revisarCuotasImpagas(idPadrino);
        return this.successResponse(modi);
    }
}
