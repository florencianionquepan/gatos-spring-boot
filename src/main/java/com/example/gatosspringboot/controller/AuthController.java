package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.AuthRequestDTO;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.service.interfaces.IJwtService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IJwtService jwtService;
    private final AuthenticationManager authManager;
    private Logger logger= LoggerFactory.getLogger(AuthController.class);

    public AuthController(IJwtService jwtService,
                          AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @PostMapping
    public String authenticateAndGetToken(@RequestBody @Valid AuthRequestDTO authRequest){
        try{
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            //logger.info("a ver: "+authentication);
            return jwtService.generateToken(authRequest.getEmail());
        }catch (Exception e){
            throw new NonExistingException("Credenciales invalidas");
        }
    }
}
