package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.PersonaDTO;
import com.example.gatosspringboot.dto.mapper.IPersonaMapper;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/personas")
@Validated
public class PersonaController {
    private final IPersonaService service;
    private final IPersonaMapper mapper;
    private final ObjectMapper objectMapper;
    public Map<String,Object> mensajeBody= new HashMap<>();
    private Logger logger= LoggerFactory.getLogger(PersonaController.class);

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
    ///personas/dni?dni={valor_dni}
    //Se debe llamar primero a este endpoint antes de crear nueva solicitud
    public ResponseEntity<?> buscarByDni(@RequestParam("dni") String dni){
        if(dni.length()!=9 || !dni.matches("\\d+")){
            return this.notSuccessResponse("El dni debe contener solo numeros sin puntos",null);
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

    @GetMapping("/search/dni")
    //puede ser utilizado por socios solamente
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> obtenerDatosByDni(@RequestParam @Pattern(regexp = "\\d{9}",
            message = "El dni debe contener exactamente 9 números sin puntos") String dni){
        PersonaDTO dto=this.mapper.mapToDto(this.service.findByDni(dni));
        return this.successResponse(dto);
    }

    @GetMapping("/search/email")
    //puede ser utilizado por socios solamente
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> obtenerDatosByEmail(@RequestParam
                                                     @Email(message="El email no es válido") String email){
        PersonaDTO dto=this.mapper.mapToDto(this.service.findByEmail(email));
        return this.successResponse(dto);
    }

}