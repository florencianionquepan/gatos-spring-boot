package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.*;
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
    private final PadrinoRepository padrinoRepo;
    private final TransitoRepository transitoRepo;
    private final VoluntarioRepository voluRepo;
    private final UsuarioRepository userRepo;
    private ConcurrentHashMap<String, Boolean> tokenCache = new ConcurrentHashMap<>();
    private Logger logger= LoggerFactory.getLogger(PersonaService.class);

    public PersonaService(PersonaRepository repo,
                          IEmailService emailService,
                          IUsuarioService userService,
                          PadrinoRepository padrinoRepo,
                          TransitoRepository transitoRepo,
                          VoluntarioRepository voluRepo,
                          UsuarioRepository userRepo) {
        this.repo = repo;
        this.emailService = emailService;
        this.userService = userService;
        this.padrinoRepo = padrinoRepo;
        this.transitoRepo = transitoRepo;
        this.voluRepo = voluRepo;
        this.userRepo = userRepo;
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

    @Override
    public Persona modificar(Persona persona, Long id) {
        this.findByIdOrException(id);
        this.dniExistenteNoPropio(id, persona.getDni());
        Optional<Usuario> oUser=this.userRepo.findByEmail(persona.getEmail());
        persona.setUsuario(oUser.get());
        persona.setId(id);
        return this.repo.save(persona);
    }

    private Persona findByIdOrException(Long id) {
        Optional<Persona> oPerso=this.repo.findById(id);
        if(oPerso.isEmpty()){
            throw new PersonNotFound(
                    String.format("La persona con id %d no existe",id)
            );
        }
        return oPerso.get();
    }

    private void dniExistenteNoPropio(Long id, String dni){
        Optional<Persona> perso=this.repo.findByDni(dni);
        if(perso.isPresent() && perso.get().getId()!=id){
            throw new ExistingException(
                    String.format("Este dni %s ya se encuentra registrado",dni)
            );
        }
    }

    @Override
    public List<String> tiposVoluntario(String dni) {
        List<String> tipos=new ArrayList<>();
        Persona perso=this.findByDni(dni);
        Optional<Padrino> oPadri=this.padrinoRepo.buscarByEmail(perso.getEmail());
        if(oPadri.isPresent()){
            tipos.add("Padrino");
        }
        Optional<Transito> oTransi=this.transitoRepo.findByEmail(perso.getEmail());
        if(oTransi.isPresent()){
            tipos.add("Transito");
        }
        Optional<Voluntario> oVolu=this.voluRepo.findByEmail(perso.getEmail());
        if(oVolu.isPresent()){
            tipos.add("Voluntario");
        }
        return tipos;
    }

    private void validarDniUnico(String dni) {
        Optional<Persona> oPerso=this.repo.findByDni(dni);
        if(oPerso.isPresent()){
            throw new ExistingException(
                    String.format("Este dni %s ya se encuentra registrado",dni)
            );
        }
    }
}
