package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final IUsuarioService userService;
    private Logger logger= LoggerFactory.getLogger(LoginController.class);

    public LoginController(IUsuarioService userService) {
        this.userService = userService;
    }

    public Usuario getUserDetailsAfterLogin(Authentication auth){
        logger.info("ingresa al /auth");
        Usuario user=userService.buscarByEmail(auth.getName());
        return user;
    }


}
