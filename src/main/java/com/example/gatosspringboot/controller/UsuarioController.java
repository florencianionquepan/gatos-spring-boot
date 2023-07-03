package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.UsuarioPasswordDTO;
import com.example.gatosspringboot.dto.UsuarioReqDTO;
import com.example.gatosspringboot.dto.UsuarioEmailDTO;
import com.example.gatosspringboot.dto.mapper.IUsuarioMapper;
import com.example.gatosspringboot.dto.mapper.IUsuarioPasswordMapper;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioMapper usMap;
    private final IUsuarioPasswordMapper usPasswordMapper;
    public Map<String,Object> mensajeBody= new HashMap<>();

    private ResponseEntity<?> notSuccessResponse(String mensaje,int id){
        mensajeBody.put("Success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }

    private ResponseEntity<?> successResponse(String email){
        mensajeBody.put("Success",Boolean.TRUE);
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
    public List<UsuarioEmailDTO> obtenerTodos(){
        return this.usMap.mapListToDto(this.usService.verTodos());
    }

    @PostMapping
    public ResponseEntity<?> nuevoUsuario(@RequestBody @Valid UsuarioReqDTO dto){
        Usuario user=this.usMap.mapToEntity(dto);
        String newUser=this.usService.altaUsuarioCompleto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping
    public ResponseEntity<?> modificarPassword(@RequestBody @Valid UsuarioPasswordDTO dto){
        Usuario user=this.usPasswordMapper.mapToEntity(dto);
        String modiUser=this.usService.modiPassword(user, dto.getPasswordActual());
        return successResponse(modiUser);
    }


}
