package com.example.gatosspringboot.config;

import com.example.gatosspringboot.model.Rol;
import com.example.gatosspringboot.repository.database.RolRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataConfig {

    private final RolRepository rolRepository;

    public InitialDataConfig(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @PostConstruct
    public void init(){
        Rol user=new Rol(1,"ROLE_USER",null);
        this.rolRepository.save(user);
        Rol voluntario=new Rol(2,"ROLE_VOLUNTARIO",null);
        this.rolRepository.save(voluntario);
        Rol socio=new Rol(2,"ROLE_SOCIO",null);
        this.rolRepository.save(socio);
    }
}
