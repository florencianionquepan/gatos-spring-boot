package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.UserResponseDTO;
import com.example.gatosspringboot.dto.UsuarioPasswordDTO;
import com.example.gatosspringboot.dto.UsuarioRespDTO;
import com.example.gatosspringboot.dto.mapper.IUserResponseMapper;
import com.example.gatosspringboot.dto.mapper.IUsuarioMapper;
import com.example.gatosspringboot.dto.mapper.IUsuarioPasswordMapper;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;
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
    private final IUserResponseMapper userMapper;
    public Map<String,Object> mensajeBody= new HashMap<>();
    private Logger logger= LoggerFactory.getLogger(UsuarioController.class);

    private ResponseEntity<?> notSuccessResponse(String mensaje,int id){
        mensajeBody.put("success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    private final IUsuarioService usService;

    public UsuarioController(IUsuarioMapper usMap,
                             IUsuarioPasswordMapper usPasswordMapper,
                             IUserResponseMapper userMapper,
                             IUsuarioService usService) {
        this.usMap = usMap;
        this.usPasswordMapper = usPasswordMapper;
        this.userMapper = userMapper;
        this.usService = usService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SOCIO')")
    public ResponseEntity<?> obtenerTodos(){
        HashMap<Usuario, Persona> usuarios=this.usService.verTodos();
        return successResponse(this.userMapper.mapToListDto(usuarios));
    }

    @PutMapping("/{id}/bloqueado")
    @PreAuthorize("hasRole('SOCIO')")
    public ResponseEntity<?> bloquearUsuario(@PathVariable Long id,
                                             @RequestBody @Valid UsuarioRespDTO dto){
        Usuario usuario=this.usService.bloquearUsuario(id, dto.getMotivo());
        return successResponse(this.usMap.mapToDto(usuario));
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
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data","Email validado correctamente." +
                "Ya puede iniciar sesion!");
        return ResponseEntity.ok(mensajeBody);
    }

    @GetMapping("/{email}/validacion")
    public ResponseEntity<?> enviarValidacion(@PathVariable String email){
        if(email==null){
            throw new NonExistingException("No enviaste ningun email!");
        }
        this.usService.enviarValidacion(email);
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data","Chequee su bandeja de entrada");
        return ResponseEntity.ok(mensajeBody);
    }


}
