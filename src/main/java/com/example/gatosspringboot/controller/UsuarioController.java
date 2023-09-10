package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.UsuarioPasswordDTO;
import com.example.gatosspringboot.dto.UsuarioEmailDTO;
import com.example.gatosspringboot.dto.mapper.IUsuarioMapper;
import com.example.gatosspringboot.dto.mapper.IUsuarioPasswordMapper;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.service.imple.UsuarioService;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioMapper usMap;
    private final IUsuarioPasswordMapper usPasswordMapper;
    public Map<String,Object> mensajeBody= new HashMap<>();
    private Logger logger= LoggerFactory.getLogger(UsuarioController.class);

    private ResponseEntity<?> notSuccessResponse(String mensaje,int id){
        mensajeBody.put("success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }

    private ResponseEntity<?> successResponse(String email){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",email);
        return ResponseEntity.ok(mensajeBody);
    }

    private final IUsuarioService usService;

    public UsuarioController(IUsuarioMapper usMap,
                             IUsuarioPasswordMapper usPasswordMapper,
                             IUsuarioService usService) {
        this.usMap = usMap;
        this.usPasswordMapper = usPasswordMapper;
        this.usService = usService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SOCIO')")
    public List<UsuarioEmailDTO> obtenerTodos(){
        return this.usMap.mapListToDto(this.usService.verTodos());
    }

    @PutMapping
    //puede modificar su password con la password actual cualquiera sea su rol
    public ResponseEntity<?> modificarPassword(@RequestBody @Valid UsuarioPasswordDTO dto){
        Usuario user=this.usPasswordMapper.mapToEntity(dto);
        String modiUser=this.usService.modiPassword(user, dto.getPasswordActual());
        return successResponse(modiUser);
    }

    @GetMapping("/{id}/validacion/{token}")
    @PermitAll
    public ResponseEntity<?> validarUsuario(@PathVariable("id") Long id,
                                            @PathVariable("token") String token){
        Boolean validado=this.usService.validarUsuario(id,token);
        return ResponseEntity.ok("Email validado correctamente." +
                "Ya puede iniciar sesion!");
    }


}
