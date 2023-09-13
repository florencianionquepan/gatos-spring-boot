package com.example.gatosspringboot;

import com.mercadopago.MercadoPagoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true,  securedEnabled = true,  jsr250Enabled = true)
public class GatosSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatosSpringBootApplication.class, args);
		MercadoPagoConfig.setAccessToken("TEST-2026244908216058-090220-0a87d4d96dab54d906b33e8aba028d95-1026358921");
	}

}
