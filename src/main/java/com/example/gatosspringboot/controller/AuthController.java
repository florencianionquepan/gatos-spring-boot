package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.AuthRequestDTO;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.service.interfaces.IJwtService;
import jakarta.validation.Valid;
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

    public AuthController(IJwtService jwtService,
                          AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @PostMapping
    public String authenticateAndGetToken(@RequestBody @Valid AuthRequestDTO authRequest){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getMail(), authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getMail());
        }else{
            throw new NonExistingException("Credenciales invalidas");
        }
    }
}
