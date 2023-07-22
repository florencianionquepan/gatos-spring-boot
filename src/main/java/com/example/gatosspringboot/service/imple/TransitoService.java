package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Transito;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.TransitoRepository;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.example.gatosspringboot.service.interfaces.ITransitoService;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransitoService implements ITransitoService {

    public final TransitoRepository repo;
    public final PersonaRepository persoRepo;
    public final IPersonaService persoService;
    public final IUsuarioService userService;

    public TransitoService(TransitoRepository repo,
                           PersonaRepository persoRepo,
                           IPersonaService persoService,
                           IUsuarioService userService) {
        this.repo = repo;
        this.persoRepo = persoRepo;
        this.persoService = persoService;
        this.userService = userService;
    }

    @Override
    public List<Transito> listarTodos() {
        return (List<Transito>) this.repo.findAll();
    }

    @Override
    public List<Transito> listarByLocalidad(String localidad) {
        return this.repo.buscarByLocalidad(localidad);
    }

    @Override
    @Transactional
    public Transito nuevo(Transito transito) {
        this.TransitoExistente(transito);
        Optional<Persona> oPerso=this.persoRepo.findByDni(transito.getDni());
        //siempre va a existir porqie ahora debe registrarse primero pero igual lo dejo
        //por si a futuro implemento el alta directa
        if(oPerso.isPresent()){
            transito.setId(oPerso.get().getId());
            this.repo.saveTransito(transito.getId());
            return transito;
        }
        return this.repo.save(transito);
    }

    @Override
    public Transito findByIdOrException(Long id) {
        Optional<Transito> oTran=this.repo.findById(id);
        if(oTran.isEmpty()){
            throw new PersonNotFound(
                    String.format("El transito con id %d no existe",id)
            );
        }
        return oTran.get();
    }

    //si ya existe con otro dni o email no prosigue-
    private void TransitoExistente(Transito transito){
        Optional<Transito> oTransitoDni=this.repo.findByDni(transito.getDni());
        if(oTransitoDni.isPresent()){
            throw new ExistingException(
                    String.format("El transito con dni %s ya existe",transito.getDni()));
        }
        Optional<Transito> oTransitoEmail=this.repo.findByEmail(transito.getEmail());
        if(oTransitoEmail.isPresent()){
            throw new ExistingException(
                    String.format("El transito con email %s ya existe",transito.getEmail()));
        }
    }
}
