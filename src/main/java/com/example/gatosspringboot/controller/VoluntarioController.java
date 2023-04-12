package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.model.Voluntario;
import com.example.gatosspringboot.service.interfaces.IVoluntarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/voluntarios")
public class VoluntarioController {

    private final IVoluntarioService volService;

    public VoluntarioController(IVoluntarioService volService) {
        this.volService = volService;
    }

    public Map<String,Object> mensajeBody= new HashMap<>();

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
        List<Voluntario> lista=this.volService.verTodos();
        return this.successResponse(lista);
    }

    @PostMapping
    public ResponseEntity<?> altaVoluntario(@RequestBody Voluntario volunt){
        Voluntario volNuevo=this.volService.altaVolunt(volunt);
        return ResponseEntity.status(HttpStatus.CREATED).body(volNuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modiVoluntario(@RequestBody Voluntario volunt,
                                         @PathVariable Long id){
        Voluntario volModi=this.volService.modiVolunt(volunt, id);
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",volModi);
        return ResponseEntity.ok(mensajeBody);
    }


}
