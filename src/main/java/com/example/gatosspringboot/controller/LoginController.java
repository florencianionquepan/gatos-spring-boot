package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final IUsuarioService userService;
    private Logger logger= LoggerFactory.getLogger(LoginController.class);

    public Map<String,Object> mensajeBody= new HashMap<>();

    public LoginController(IUsuarioService userService) {
        this.userService = userService;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    @RequestMapping("/auth")
    public ResponseEntity<?> getUserDetailsAfterLogin(Authentication authentication){
        Usuario user=userService.buscarByEmail(authentication.getName());
        return this.successResponse(user);
    }


}
