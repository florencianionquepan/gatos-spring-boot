package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.VoluntarioDTO;
import com.example.gatosspringboot.dto.mapper.IVoluntarioMapper;
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
    private final IVoluntarioMapper mapper;

    public VoluntarioController(IVoluntarioService volService,
                                IVoluntarioMapper mapper) {
        this.volService = volService;
        this.mapper = mapper;
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
    public ResponseEntity<?> altaVoluntario(@RequestBody VoluntarioDTO volunt){
        Voluntario volNuevo=this.volService.altaVolunt(this.mapper.mapToEntity(volunt));
        VoluntarioDTO dtoNuevo=this.mapper.mapToDto(volNuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoNuevo);
    }

    @PutMapping("/{id}")
    //Se usa para modificar algun dato personal o agregar/remover gatos de su lista
    public ResponseEntity<?> modiVoluntario(@RequestBody Voluntario volunt,
                                         @PathVariable Long id){
        Voluntario volModi=this.volService.modiVolunt(volunt, id);
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",volModi);
        return ResponseEntity.ok(mensajeBody);
    }


}
