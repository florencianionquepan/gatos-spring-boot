package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.mapper.IUserResponseMapper;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final IUsuarioService userService;
    private final IUserResponseMapper mapper;
    private final IPersonaService persoSer;
    private Logger logger= LoggerFactory.getLogger(LoginController.class);

    public Map<String,Object> mensajeBody= new HashMap<>();

    public LoginController(IUsuarioService userService,
                           IUserResponseMapper mapper,
                           IPersonaService persoSer) {
        this.userService = userService;
        this.mapper = mapper;
        this.persoSer = persoSer;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    @RequestMapping("/auth")
    public ResponseEntity<?> getUserDetailsAfterLogin(Authentication authentication){
        Usuario user=userService.buscarByEmail(authentication.getName());
        Persona perso=this.persoSer.findByEmailOrException(authentication.getName());
        return this.successResponse(this.mapper.mapToDTO(user,perso));
    }

}
