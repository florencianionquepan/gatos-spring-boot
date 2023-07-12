package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Solicitud;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PersonaService implements IPersonaService {

    private final PersonaRepository repo;
    private ConcurrentHashMap<String, Boolean> tokenCache = new ConcurrentHashMap<>();

    public PersonaService(PersonaRepository repo) {
        this.repo = repo;
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

}
