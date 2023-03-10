package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gatos")
public class GatoController {

    private final IGatoService gatoSer;

    public Map<String,Object> mensajeBody= new HashMap<>();

    public GatoController(IGatoService gatoSer) {
        this.gatoSer = gatoSer;
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
        return this.successResponse(this.gatoSer.verTodos());
    }

    @PostMapping
    public ResponseEntity<?> altaGato(@RequestBody Gato gato){
        Gato nuevo=this.gatoSer.altaGato(gato);
        if(nuevo==null){
            return this.notSuccessResponse("El gato no pudo ser creado",0);
        }
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeBody);
    }
}
