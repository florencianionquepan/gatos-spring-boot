package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.service.interfaces.IEmailService;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PersonaService implements IPersonaService {

    private final PersonaRepository repo;
    private final IEmailService emailService;
    private final IUsuarioService userService;
    private ConcurrentHashMap<String, Boolean> tokenCache = new ConcurrentHashMap<>();
    private Logger logger= LoggerFactory.getLogger(PersonaService.class);

    public PersonaService(PersonaRepository repo,
                          IEmailService emailService,
                          IUsuarioService userService) {
        this.repo = repo;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public Persona findByDni(String dni) {
        Optional<Persona> oPerso=this.repo.findByDni(dni);
        if(oPerso.isEmpty()){
            throw new PersonNotFound(
                    String.format("La persona con dni %s no existe",dni)
            );
        }
        return oPerso.get();
    }

    @Override
    public Persona findByEmailOrException(String email) {
        Optional<Persona> oPerso=this.repo.findByEmail(email);
        if(oPerso.isEmpty()){
            throw new PersonNotFound(
                    String.format("La persona con email %s no existe",email)
            );
        }
        return oPerso.get();
    }

    @Override
    public boolean existeByDni(String dni) {
        boolean existe=false;
        Optional<Persona> oPerso=this.repo.findByDni(dni);
        if(oPerso.isPresent()){
            existe=true;
        }
        return existe;
    }

    @Override
    public void validarEmailUnico(String email) {
        Optional<Persona> oPerso=this.repo.findByEmail(email);
        if(oPerso.isPresent()){
            throw new NonExistingException(
                    String.format("Este email %s ya se encuentra registrado",email)
            );
        }
    }

    @Override
    public boolean validarEmailIngresado(String email) {
        return false;
    }

    @Override
    public Persona altaRegistro(Persona persona, String token) {
        return null;
    }

    @Override
    public Persona registro(Persona persona) {
        this.validarDniUnico(persona.getDni());
        Usuario creado=this.userService.altaUsuario(persona.getUsuario());
        persona.setUsuario(creado);
        return this.repo.save(persona);
    }

    private void validarDniUnico(String dni) {
        Optional<Persona> oPerso=this.repo.findByDni(dni);
        if(oPerso.isPresent()){
            throw new NonExistingException(
                    String.format("Este dni %s ya se encuentra registrado",dni)
            );
        }
    }
}
