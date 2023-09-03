package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.service.interfaces.ICuotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cuotas")
public class CuotaController {

    private final ICuotaService service;

    public Map<String,Object> mensajeBody= new HashMap<>();

    public CuotaController(ICuotaService service) {
        this.service = service;
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
    public ResponseEntity<?> verByPadrino(Padrino padrino){
        return null;
    }

    @PostMapping
    public ResponseEntity<?> pagoCuota(@RequestBody Cuota cuota){
            Cuota cuotaPaga=this.service.nuevaCuota(cuota);
            return this.successResponse(cuotaPaga);
    }
}
