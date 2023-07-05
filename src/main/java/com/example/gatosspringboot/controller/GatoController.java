package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.GatoDTO;
import com.example.gatosspringboot.dto.mapper.IGatoMapper;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gatos")
public class GatoController {

    private final IGatoService gatoSer;
    private final IGatoMapper mapper;

    public Map<String,Object> mensajeBody= new HashMap<>();

    public GatoController(IGatoService gatoSer,
                          IGatoMapper mapper) {
        this.gatoSer = gatoSer;
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
    public ResponseEntity<?> verTodos(){
        List<Gato> gatos=this.gatoSer.verTodos();
        return this.successResponse(this.mapper.mapListToDto(gatos));
    }

    @PostMapping
    public ResponseEntity<?> altaGato(@RequestBody @Valid GatoDTO dto){
        Gato nuevo=this.gatoSer.altaGato(this.mapper.mapToEntity(dto));
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",this.mapper.mapToDto(nuevo));
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeBody);
    }

    @PutMapping("/id")
    public ResponseEntity<?> modiGato(@RequestBody GatoDTO dto,
                                      @PathVariable Long id){
        Gato modi=this.gatoSer.modiGato(this.mapper.mapToEntity(dto),id);
        GatoDTO resp=this.mapper.mapToDto(modi);
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",resp);
        return ResponseEntity.ok(mensajeBody);
    }
}
