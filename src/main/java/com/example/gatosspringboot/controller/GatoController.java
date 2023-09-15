package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.*;
import com.example.gatosspringboot.dto.mapper.IFichaMapper;
import com.example.gatosspringboot.dto.mapper.IGatoMapper;
import com.example.gatosspringboot.dto.mapper.ITransitoIdMapper;
import com.example.gatosspringboot.dto.mapper.IVoluntarioEmailMapper;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final IFichaMapper fichaMap;
    private final ITransitoIdMapper transitoMap;
    private final IVoluntarioEmailMapper voluMapper;

    public Map<String,Object> mensajeBody= new HashMap<>();

    public GatoController(IGatoService gatoSer,
                          IGatoMapper mapper,
                          IFichaMapper fichaMap,
                          ITransitoIdMapper transitoMap,
                          IVoluntarioEmailMapper voluMapper) {
        this.gatoSer = gatoSer;
        this.mapper = mapper;
        this.fichaMap = fichaMap;
        this.transitoMap = transitoMap;
        this.voluMapper = voluMapper;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    private ResponseEntity<?> notSuccessResponse(String mensaje,int id){
        mensajeBody.put("success",Boolean.FALSE);
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

    @GetMapping("/{id}")
    public ResponseEntity<?> verById(@PathVariable Long id){
        Gato gato=this.gatoSer.verById(id);
        return this.successResponse(this.mapper.mapToDto(gato));
    }

    @GetMapping("/voluntarios/{email}")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> verByVoluntario(@PathVariable String email){
        List<Gato> gatos=this.gatoSer.verByVoluntario(email);
        return this.successResponse(this.mapper.mapListToDto(gatos));
    }


    @PostMapping
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> altaGato(@RequestBody @Valid GatoDTO dto){
        Gato nuevo=this.gatoSer.altaGato(this.mapper.mapToEntity(dto));
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",this.mapper.mapToDto(nuevo));
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeBody);
    }

    @PutMapping("/{id}/ficha")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> agregarFicha(@RequestBody FichaDTO ficha,
                                          @PathVariable Long id){
        Gato modi=this.gatoSer.agregarFicha(this.fichaMap.mapToEntity(ficha), id);
        return this.successResponse(this.mapper.mapToDto(modi));
    }

    @PutMapping("/{id}/transito")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> agregarTransito(@RequestBody TransitoIdDTO dto,
                                             @PathVariable Long id){
        Gato modi=this.gatoSer.agregarTransito(this.transitoMap.mapToEntity(dto), id);
        return this.successResponse(this.mapper.mapToDto(modi));
    }

    @PutMapping("/id")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> modiGato(@RequestBody GatoDTO dto,
                                      @PathVariable Long id){
        Gato modi=this.gatoSer.modiGato(this.mapper.mapToEntity(dto),id);
        GatoRespDTO resp=this.mapper.mapToDto(modi);
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",resp);
        return ResponseEntity.ok(mensajeBody);
    }
}
