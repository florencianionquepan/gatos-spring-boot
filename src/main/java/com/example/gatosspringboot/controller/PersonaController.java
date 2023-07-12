package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.mapper.IPersonaMapper;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/personas")
public class PersonaController {
    private final IPersonaService service;
    private final IPersonaMapper mapper;
    public Map<String,Object> mensajeBody= new HashMap<>();

    public PersonaController(IPersonaService service, IPersonaMapper mapper) {
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


    @GetMapping("/dni")
    ///personas/search?dni={valor_dni}
    //Se debe llamar primero a este endpoint antes de crear nueva solicitud
    public ResponseEntity<?> buscarByDni(@RequestParam("dni") String dni){
        if(dni.length()!=9 || !dni.matches("\\d+")){
            this.notSuccessResponse("El dni debe contener solo numeros sin puntos",null);
        }
        if(!this.service.personaExistente(dni)){
            return ResponseEntity.notFound().build();
        }
        return this.successResponse("Se envió a su email un token para validar su identidad");
    }

}
