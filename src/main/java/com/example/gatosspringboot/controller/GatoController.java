package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.*;
import com.example.gatosspringboot.dto.mapper.IFichaMapper;
import com.example.gatosspringboot.dto.mapper.IGatoMapper;
import com.example.gatosspringboot.dto.mapper.ITransitoIdMapper;
import com.example.gatosspringboot.dto.mapper.IVoluntarioEmailMapper;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Ficha;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/gatos")
public class GatoController {

    private final IGatoService gatoSer;
    private final IGatoMapper mapper;
    private final IFichaMapper fichaMap;
    private final ITransitoIdMapper transitoMap;
    private final IVoluntarioEmailMapper voluMapper;

    public Map<String,Object> mensajeBody= new HashMap<>();
    private Logger logger= LoggerFactory.getLogger(GatoController.class);

    public GatoController(IGatoService gatoSer,
                          IGatoMapper mapper,
                          IFichaMapper fichaMap,
                          ITransitoIdMapper transitoMap,
                          IVoluntarioEmailMapper voluMapper) {
        this.gatoSer = gatoSer;
        this.mapper = mapper;
        this.fichaMap = fichaMap;
        this.transitoMap = transitoMap;
        this.voluMapper = voluMapper;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    private ResponseEntity<?> notSuccessResponse(String mensaje,int id){
        mensajeBody.put("success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }

    private GatoDTO obtenerJsonValido(String dto){
        ObjectMapper objectMapper = new ObjectMapper();
        GatoDTO dtoJson;
        try{
            dtoJson=objectMapper.readValue(dto,GatoDTO.class);
        }catch(Exception ex){
            logger.info("ex="+ex);
            throw new NonExistingException("Error en la deserialización del gato");
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<GatoDTO>> violations = validator.validate(dtoJson);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return dtoJson;
    }

    private FichaDTO obtenerFichaDto(String ficha){
        ObjectMapper objectMapper = new ObjectMapper();
        FichaDTO dto;
        try{
            dto=objectMapper.readValue(ficha,FichaDTO.class);
            objectMapper.registerModule(new JavaTimeModule());
        }catch (Exception ex){
            //logger.info("ex="+ex);
            throw new NonExistingException("Error en la deserialización de ficha");
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<FichaDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return dto;
    }

    //se usa para el post solamente
    private ResponseEntity<?> validarFiles(MultipartFile[] multipartFiles){
        if (multipartFiles == null || multipartFiles.length == 0) {
            //return this.notSuccessResponse("No se proporciono ninguna imagen del gatito",0);
        }else{
            for (MultipartFile multipartFile : multipartFiles){
                BufferedImage bi= null;
                try {
                    bi = ImageIO.read(multipartFile.getInputStream());
                } catch (IOException e) {
                    return this.notSuccessResponse(e.getMessage(),0);
                }
                if(bi==null){
                    return this.notSuccessResponse("Alguna imagen no es válida",0);
                }
            }
        }
        return ResponseEntity.ok("imagenes ok");
    }

    @GetMapping
    public ResponseEntity<?> verTodos(){
        List<Gato> gatos=this.gatoSer.verTodos();
        return this.successResponse(this.mapper.mapListToDto(gatos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> verById(@PathVariable Long id){
        Gato gato=this.gatoSer.verById(id);
        return this.successResponse(this.mapper.mapToDto(gato));
    }
    @GetMapping("/{id}/ficha")
    public ResponseEntity<?> verFichaById(@PathVariable Long id){
        Ficha ficha=this.gatoSer.verFichaByGato(id);
        return this.successResponse(ficha);
    }


    @GetMapping("/voluntarios/{email}")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> verByVoluntario(@PathVariable String email){
        List<Gato> gatos=this.gatoSer.verByVoluntario(email);
        return this.successResponse(this.mapper.mapListToDto(gatos));
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> altaGato(@RequestPart @Valid String dto,
                                      @RequestParam(required = false) MultipartFile[] multipartFiles) {
        GatoDTO dtoJson=this.obtenerJsonValido(dto);
        ResponseEntity<?> response=this.validarFiles(multipartFiles);
        if(response.getStatusCode()!=HttpStatus.OK){
            return response;
        }
        Gato nuevo=this.gatoSer.altaGato(this.mapper.mapToEntity(dtoJson), multipartFiles);
        mensajeBody.put("Success",Boolean.TRUE);
        mensajeBody.put("data",this.mapper.mapToDto(nuevo));
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeBody);
    }

    @PutMapping("/{id}/ficha")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> agregarFicha(@RequestPart String ficha,
                                          @RequestParam(required = false) MultipartFile pdf,
                                          @PathVariable Long id){
        FichaDTO dto=this.obtenerFichaDto(ficha);
        Gato modi=this.gatoSer.agregarFicha(this.fichaMap.mapToEntity(dto),pdf, id);
        return this.successResponse(this.mapper.mapToDto(modi));
    }

    @PutMapping("/{id}/transito")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> agregarTransito(@RequestBody TransitoIdDTO dto,
                                             @PathVariable Long id){
        Gato modi=this.gatoSer.agregarTransito(this.transitoMap.mapToEntity(dto), id);
        return this.successResponse(this.mapper.mapToDto(modi));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<?> modiGato(@RequestPart @Valid String dto,
                                      @RequestParam(required = false) MultipartFile[] multipartFiles,
                                      @PathVariable Long id){
        GatoDTO dtoJson=this.obtenerJsonValido(dto);
        Gato gato=this.mapper.mapToEntity(dtoJson);
        Gato modi=this.gatoSer.modiGato(gato,multipartFiles,id);
        GatoRespDTO resp=this.mapper.mapToDto(modi);
        return this.successResponse(resp);
    }
}
