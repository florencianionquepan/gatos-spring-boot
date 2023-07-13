package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.mapper.IPersonaMapper;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/personas")
public class PersonaController {
    private final IPersonaService service;
    private final IPersonaMapper mapper;
    private final ObjectMapper objectMapper;
    public Map<String,Object> mensajeBody= new HashMap<>();

    public PersonaController(IPersonaService service, IPersonaMapper mapper,
                             ObjectMapper objectMapper) {
        this.service = service;
        this.mapper = mapper;
        this.objectMapper = new ObjectMapper();
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

    @PostMapping("/dni/{dni}/token")
    public ResponseEntity<?> validarToken(@PathVariable String dni, @RequestBody @NotEmpty String json) {
        try{
            JsonNode token=this.objectMapper.readTree(json);
            if(!token.has("token")){
                return this.notSuccessResponse("No existe un campo token",null);
            }
            String tokenValue=token.get("token").textValue();
            Persona datosPersona=this.service.datosPersona(tokenValue, dni);
            return this.successResponse(this.mapper.mapToDto(datosPersona));
        }catch (JsonProcessingException e) {
            return this.notSuccessResponse(e.getMessage(), null);
        }
    }

}