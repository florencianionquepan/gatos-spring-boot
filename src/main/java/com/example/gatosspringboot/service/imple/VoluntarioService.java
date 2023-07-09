package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.model.Voluntario;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.VoluntarioRepository;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import com.example.gatosspringboot.service.interfaces.IVoluntarioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class VoluntarioService implements IVoluntarioService {

    private final VoluntarioRepository voluRepo;
    private final IUsuarioService serUser;
    private final PersonaRepository persoRepo;
    private Logger logger= LoggerFactory.getLogger(VoluntarioService.class);

    public VoluntarioService(VoluntarioRepository voluRepo,
                             IUsuarioService serUser,
                             PersonaRepository persoRepo) {
        this.voluRepo = voluRepo;
        this.serUser = serUser;
        this.persoRepo = persoRepo;
    }

    @Override
    public List<Voluntario> verTodos() {
        return (List<Voluntario>) this.voluRepo.findAll();
    }

    @Override
    @Transactional
    public Voluntario altaVolunt(Voluntario vol) {
        Usuario creado=this.serUser.altaUsuarioVoluntario(vol.getEmail());
        //logger.info("Usuario creado: "+creado);
        //logger.info("Voluntario: "+vol);
        vol.setUsuario(creado);
        Optional<Persona> oPersona=this.persoRepo.findById(vol.getId());
        if(oPersona.isEmpty()){
            return this.voluRepo.save(vol);
        }
        this.voluRepo.saveVoluntario(vol.getId(),creado.getId());
        return vol;
    }

    @Override
    public Voluntario modiVolunt(Voluntario vol, Long id) {
        if(!this.voluRepo.existsById(id)){
            throw new NonExistingException(
                    String.format("El voluntario con id %d no existe",
                            id)
            );
        }
        if(this.existeDniConOtroId(vol.getDni(),id)){
            throw new RuntimeException(
                    String.format("El dni %d corresponde a otro voluntario"
                            ,vol.getDni())
            );
        }
        vol.setId(id);
        return this.voluRepo.save(vol);
    }

    @Override
    public boolean existeVol(Long id) {
        return this.voluRepo.existsById(id);
    }

    @Override
    public Voluntario buscarVolByEmail(String email) {
        Optional<Voluntario> oVolu=this.voluRepo.findByEmail(email);
        if(oVolu.isEmpty()){
            throw new NonExistingException(
                    String.format("El voluntario con email %d no existe",
                            email)
            );
        }
        return oVolu.get();
    }

    private boolean existeDni(String dni){
        boolean existe=false;
        Optional<Voluntario> oVolu=this.voluRepo.findByDni(dni);
        if(oVolu.isPresent()){
            existe=true;
        }
        return existe;
    }

    private boolean existeDniConOtroId(String dni, Long id){
        boolean existe=true;
        Optional<Voluntario> oVolu=this.voluRepo.findByDni(dni);
        Optional<Voluntario> oVoluId=this.voluRepo.findById(id);
        //Si se modifica el dni por uno que aun no existe
        if(oVolu.isEmpty()){
            existe=false;
        }
        //si el dni existe, pero se esta tratando del mismo id:
        if(oVolu.isPresent() && oVoluId.get().getId().equals(oVoluId.get())){
            existe=false;
        }
        return existe;
    }

}
