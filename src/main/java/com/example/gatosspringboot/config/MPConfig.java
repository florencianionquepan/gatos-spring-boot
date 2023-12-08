package com.example.gatosspringboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MPConfig {

    @Value("${mercadoPago.accessToken}")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
