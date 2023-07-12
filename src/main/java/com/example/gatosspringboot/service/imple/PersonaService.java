package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Solicitud;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.service.interfaces.IEmailService;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PersonaService implements IPersonaService {

    private final PersonaRepository repo;
    private final IEmailService emailService;
    private ConcurrentHashMap<String, Boolean> tokenCache = new ConcurrentHashMap<>();

    public PersonaService(PersonaRepository repo,
                          IEmailService emailService) {
        this.repo = repo;
        this.emailService = emailService;
    }

    @Override
    public Persona findByDni(String dni) {
        Optional<Persona> oPerso=this.repo.findByDni(dni);
        if(oPerso.isEmpty()){
            throw new NonExistingException(
                    String.format("La persona no existe")
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
    public void addSolicitudPersona(Solicitud solicitud) {
        Persona solicitante= solicitud.getSolicitante();
        if(this.existeByDni(solicitante.getDni())){
            Persona perso=this.findByDni(solicitante.getDni());
            List<Solicitud> solicitudes=perso.getSolicitudesAdopcion();
            solicitudes.add(solicitud);
            perso.setSolicitudesAdopcion(solicitudes);
            solicitud.setSolicitante(perso);
        }else{
            solicitante.setSolicitudesAdopcion(new ArrayList<>(Arrays.asList(solicitud)));
            this.repo.save(solicitante);
        }
    }

    @Override
    public boolean personaExistente(String dni) {
        Optional<Persona> oPerso=this.repo.findByDni(dni);
        if(oPerso.isPresent()){
            //envio token a su email personal
            String token=this.generarToken();
            Persona persona=oPerso.get();
            String text="Hola "+persona.getNombre()+"!. \\nTe enviamos el codigo que debes ingresar " +
                    "para validar tu identidad y poder rellenar los campos en nuestro formulario."+
                    "\\nSi no has intentado enviar una solicitud para formar parte de Gatshan, por favor ignora este mensaje" +
                    "\\n Código: "+token;
            this.emailService.sendMessage(persona.getEmail(), "Valida tu identidad",text);
        }
        return oPerso.isPresent();
    }

    @Override
    public Persona datosPersona(String token, String dni) {
        if(!this.validarToken(token)){
            throw new NonExistingException(
                    "El código no coincide, Por favor ingresa tu dni nuevamente."
            );
        }else{
            return this.findByDni(dni);
        }
    }

    private String generarToken() {
        String token = UUID.randomUUID().toString();
        tokenCache.put(token, true);
        return token;
    }

    private boolean validarToken(String token) {
        return tokenCache.containsKey(token);
    }
}
