package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Socio;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.SocioRepository;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import com.example.gatosspringboot.service.interfaces.ISocioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocioService implements ISocioService {

    private final SocioRepository repo;
    private final PersonaRepository persoRepo;
    private final PersonaService persoService;
    private final UsuarioRepository usRepo;
    private final UsuarioService usService;

    public SocioService(SocioRepository repo,
                        PersonaRepository persoRepo,
                        PersonaService persoService,
                        UsuarioRepository usRepo,
                        UsuarioService usService) {
        this.repo = repo;
        this.persoRepo = persoRepo;
        this.persoService = persoService;
        this.usRepo = usRepo;
        this.usService = usService;
    }

    @Override
    public Socio buscarByEmailOrException(String email) {
        Optional<Socio> oSocio = this.repo.findByEmail(email);
        if (oSocio.isEmpty()) {
            throw new NonExistingException(
                    String.format("No existe un socio con email %s", email));
        }
        return oSocio.get();
    }

    @Override
    @Transactional
    //le dara de alta otro socio
    public Socio altaSocio(Socio socio) {
        this.socioExistente(socio);
        Optional<Persona> oPersoDni=this.persoRepo.findByDni(socio.getPersona().getDni());
        Optional<Usuario> oUser=this.usRepo.findByEmail(socio.getPersona().getEmail());
        if(oPersoDni.isEmpty()){
            this.persoService.validarEmailUnico(socio.getPersona().getEmail());
            Usuario admin=this.usService.altaUsuarioSocio(socio.getPersona().getEmail());
            socio.getPersona().setUsuario(admin);
            return this.repo.save(socio);
        }
        //si ya existe como persona
        //si ya tiene el rol admin no se vuelve a agregar
        Usuario user=oUser.isEmpty()?this.usService.altaUsuarioSocio(socio.getPersona().getEmail())
                :this.usService.agregarRolSocio(oUser.get());
        Persona perso=oPersoDni.get();
        socio.getPersona().setUsuario(user);
        this.repo.save(socio);
        return socio;
    }

    //si ya existe con otro dni o email no prosigue-
    private void socioExistente(Socio socio){
        Optional<Socio> oSocioDni=this.repo.findByDni(socio.getPersona().getDni());
        if(oSocioDni.isPresent()){
            throw new ExistingException(
                    String.format("El socio con dni %s ya existe",socio.getPersona().getDni()));
        }
        Optional<Socio> oSocioEmail=this.repo.findByEmail(socio.getPersona().getEmail());
        if(oSocioEmail.isPresent()){
            throw new ExistingException(
                    String.format("El socio con email %s ya existe",socio.getPersona().getEmail()));
        }
    }

    @Override
    public List<Socio> listarTodos() {
        return (List<Socio>) this.repo.findAll();
    }
}
