package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.FichaDTO;
import com.example.gatosspringboot.dto.GatoDTO;
import com.example.gatosspringboot.dto.TransitoIdDTO;
import com.example.gatosspringboot.dto.mapper.IFichaMapper;
import com.example.gatosspringboot.dto.mapper.IGatoMapper;
import com.example.gatosspringboot.dto.mapper.ITransitoIdMapper;
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
    private final IFichaMapper fichaMap;
    private final ITransitoIdMapper transitoMap;

    public Map<String,Object> mensajeBody= new HashMap<>();

    public GatoController(IGatoService gatoSer,
                          IGatoMapper mapper,
                          IFichaMapper fichaMap,
                          ITransitoIdMapper transitoMap) {
        this.gatoSer = gatoSer;
        this.mapper = mapper;
        this.fichaMap = fichaMap;
        this.transitoMap = transitoMap;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",data);
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
    //@PreAuthorize("hasAuthority('ROLE_VOLUNTARIO')")
    public ResponseEntity<?> altaGato(@RequestBody @Valid GatoDTO dto){
        Gato nuevo=this.gatoSer.altaGato(this.mapper.mapToEntity(dto));
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",this.mapper.mapToDto(nuevo));
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeBody);
    }

    @PutMapping("/id/ficha")
    //@PreAuthorize("hasAuthority('ROLE_VOLUNTARIO')")
    public ResponseEntity<?> agregarFicha(@RequestBody FichaDTO ficha,
                                          @PathVariable Long id){
        Gato modi=this.gatoSer.agregarFicha(this.fichaMap.mapToEntity(ficha), id);
        return this.successResponse(this.mapper.mapToDto(modi));
    }

    @PutMapping("/id/transito")
    //@PreAuthorize("hasAuthority('ROLE_VOLUNTARIO')")
    public ResponseEntity<?> agregarTransito(@RequestBody TransitoIdDTO dto,
                                             @PathVariable Long id){
        Gato modi=this.gatoSer.agregarTransito(this.transitoMap.mapToEntity(dto), id);
        return this.successResponse(this.mapper.mapToDto(modi));
    }

    @PutMapping("/id")
    //@PreAuthorize("hasAuthority('ROLE_VOLUNTARIO')")
    public ResponseEntity<?> modiGato(@RequestBody GatoDTO dto,
                                      @PathVariable Long id){
        Gato modi=this.gatoSer.modiGato(this.mapper.mapToEntity(dto),id);
        GatoDTO resp=this.mapper.mapToDto(modi);
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",resp);
        return ResponseEntity.ok(mensajeBody);
    }
}
