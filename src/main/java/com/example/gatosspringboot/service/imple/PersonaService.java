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
        Optional<Persona> oPerso=this.repo.findPersonByEmail(email);
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
        Optional<Persona> oPerso=this.repo.findPersonByEmail(email);
        if(oPerso.isPresent()){
            throw new NonExistingException(
                    String.format("Este email %s ya se encuentra registrado",email)
            );
        }
    }

    @Override
    public boolean validarEmailIngresado(String email) {
        this.validarEmailUnico(email);
        String token=this.generarToken();
        String text="Hola!" + "\nTe enviamos el siguiente codigo para " +
                "validar tu identidad y poder crear una cuenta"+
                "\nSi no has intentado enviar una solicitud " +
                "para formar parte de Gatshan, por favor ignora este mensaje" +
                "\n Código: "+token;
        return this.emailService.sendMessage(email, "Valida tu identidad",text);
    }

    @Override
    public Persona altaRegistro(Persona persona, String token) {
        if(!this.validarToken(token)){
            throw new NonExistingException(
                    "El código no coincide, Por favor ingresalo nuevamente."
            );
        }
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

    private String generarToken() {
        String token = UUID.randomUUID().toString();
        tokenCache.put(token, true);
        //logger.info("token cache= "+tokenCache);
        return token;
    }

    private boolean validarToken(String token) {
        return tokenCache.containsKey(token);
    }
}
