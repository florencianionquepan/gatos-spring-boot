package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.AspiranteDTO;
import com.example.gatosspringboot.dto.mapper.IAspiranteMapper;
import com.example.gatosspringboot.model.Aspirante;
import com.example.gatosspringboot.service.interfaces.IAspiranteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aspirantes")
public class AspiranteController {

    private final IAspiranteService service;
    private final IAspiranteMapper mapper;
    public Map<String,Object> mensajeBody= new HashMap<>();

    public AspiranteController(IAspiranteService service,
                               IAspiranteMapper mapper) {
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

    @PostMapping
    public ResponseEntity<?> nuevo(@RequestBody @Valid AspiranteDTO dto){
        Aspirante creado=this.service.altaAspirante(this.mapper.mapToEntity(dto));
        AspiranteDTO dtoCreado=this.mapper.mapToDto(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreado);
    }

    @GetMapping
    public ResponseEntity<?> verTodas(){
        List<AspiranteDTO> dtos=this.mapper.mapToListDto(this.service.listarTodos());
        return successResponse(dtos);
    }
}
