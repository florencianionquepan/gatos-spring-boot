package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.PersonaDTO;
import com.example.gatosspringboot.dto.RegistroDTO;
import com.example.gatosspringboot.dto.mapper.IPersonaMapper;
import com.example.gatosspringboot.dto.mapper.IRegistroMapper;
import com.example.gatosspringboot.dto.mapper.IUsuarioEmailMapper;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @PostMapping
    @PermitAll
    public ResponseEntity<?> nueva(@RequestBody @Valid RegistroDTO dto){
        Persona nueva=this.service.registro(this.registroMapper.mapToEntity(dto));
        return this.successResponse(this.mapper.mapToDto(nueva));
    }

    @PutMapping("/{id}")
    @PreAuthorize("#dto.email==authentication.principal")
    public ResponseEntity<?> actualizarDatos(@RequestBody @Valid PersonaDTO dto, @PathVariable Long id){
        Persona modi=this.service.modificar(this.mapper.mapToPersona(dto), id);
        return this.successResponse(this.mapper.mapToDto(modi));
    }

    @GetMapping("/search/dni")
    //puede ser utilizado por socios solamente y voluntarios
    @PreAuthorize("hasAnyRole('SOCIO', 'VOLUNTARIO')")
    public ResponseEntity<?> obtenerDatosByDni(@RequestParam @Pattern(regexp = "\\d{8}",
            message = "El dni debe contener exactamente 8 números sin puntos") String dni){
        PersonaDTO dto=this.mapper.mapToDto(this.service.findByDni(dni));
        return this.successResponse(dto);
    }

    @GetMapping("/search")
    //puede ser utilizado por socios y voluntarios solamente
    //@PreAuthorize("hasAnyRole('SOCIO', 'VOLUNTARIO')")
    @PreAuthorize("#email==authentication.principal")
    public ResponseEntity<?> obtenerDatosByEmail(@RequestParam
                                                     @Email(message="El email no es válido") String email){
        PersonaDTO dto=this.mapper.mapToDto(this.service.findByEmailOrException(email));
        return this.successResponse(dto);
    }

    @GetMapping("/{dni}/voluntariado")
    @PreAuthorize("hasAnyRole('SOCIO', 'VOLUNTARIO')")
    public ResponseEntity<?> esVoluntario(@PathVariable @Pattern(regexp = "\\d{8}",
            message = "El dni debe contener exactamente 8 números sin puntos") String dni){
        List<String> data=this.service.tiposVoluntario(dni);
        return this.successResponse(data);
    }

}