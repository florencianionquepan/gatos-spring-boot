package com.example.gatosspringboot.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String generateToken(String mail);
    String extractUsername(String token);
    Boolean validateToken(String token, UserDetails userDetails);
}
