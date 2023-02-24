package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.UsuarioReqDTO;
import com.example.gatosspringboot.dto.UsuarioRespDTO;
import com.example.gatosspringboot.dto.mapper.IUsuarioMapper;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioMapper usMap;
    public Map<String,Object> mensajeBody= new HashMap<>();

    private ResponseEntity<?> notSuccessResponse(String mensaje,int id){
        mensajeBody.put("Success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }

    private final IUsuarioService usService;

    public UsuarioController(IUsuarioMapper usMap, IUsuarioService usService) {
        this.usMap = usMap;
        this.usService = usService;
    }

    @GetMapping
    public List<UsuarioRespDTO> obtenerTodos(){
        return this.usMap.mapListToDto(this.usService.verTodos());
    }

    @PostMapping
    public ResponseEntity<?> nuevoUsuario(@RequestBody UsuarioReqDTO dto){
        Usuario user=this.usMap.mapToEntity(dto);
        if(this.usService.altaUsuario(user)==null){
            return this.notSuccessResponse("El usuario no pudo ser creado", 0);
        }
        Usuario newUser=this.usService.altaUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.usMap.mapToDto(newUser));
    }


}