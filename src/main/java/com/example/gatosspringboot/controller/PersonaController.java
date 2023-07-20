package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.PersonaDTO;
import com.example.gatosspringboot.dto.RegistroDTO;
import com.example.gatosspringboot.dto.UsuarioEmailDTO;
import com.example.gatosspringboot.dto.mapper.IPersonaMapper;
import com.example.gatosspringboot.dto.mapper.IRegistroMapper;
import com.example.gatosspringboot.dto.mapper.IUsuarioEmailMapper;
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
import org.springframework.http.HttpStatus;
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
    private final IUsuarioEmailMapper userMapper;
    private final IRegistroMapper registroMapper;
    private final ObjectMapper objectMapper;
    public Map<String,Object> mensajeBody= new HashMap<>();
    private Logger logger= LoggerFactory.getLogger(PersonaController.class);

    public PersonaController(IPersonaService service,
                             IPersonaMapper mapper,
                             IUsuarioEmailMapper userMapper,
                             IRegistroMapper registroMapper,
                             ObjectMapper objectMapper) {
        this.service = service;
        this.mapper = mapper;
        this.userMapper = userMapper;
        this.registroMapper = registroMapper;
        this.objectMapper = objectMapper;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    private ResponseEntity<?> notSuccessResponse(String mensaje,Integer id){
        mensajeBody.put("success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }

    @GetMapping("/validacion")
    public ResponseEntity<?> validarEmail(@RequestBody UsuarioEmailDTO dto){
        String email=this.userMapper.mapToEntity(dto).getEmail();
        if(this.service.validarEmailIngresado(email)){
            return this.successResponse("El codigo fue enviado a su email");
        }else{
            mensajeBody.put("error",HttpStatus.INTERNAL_SERVER_ERROR.value());
            mensajeBody.put("data","No se pudo enviar el código al email ingresado."+
                    "Asegurase que es un email valido o intente nuevamente");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(mensajeBody);
        }
    }

    @PostMapping
    public ResponseEntity<?> nueva(@RequestBody @Valid RegistroDTO dto){
        String tokenValue=dto.getToken();
        Persona nueva=this.service.altaRegistro(this.registroMapper.mapToEntity(dto),tokenValue);
        return this.successResponse(this.mapper.mapToDto(nueva));
    }

    @GetMapping("/search/dni")
    //puede ser utilizado por socios solamente
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> obtenerDatosByDni(@RequestParam @Pattern(regexp = "\\d{8}",
            message = "El dni debe contener exactamente 8 números sin puntos") String dni){
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