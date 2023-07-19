package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Transito;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.TransitoRepository;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.example.gatosspringboot.service.interfaces.ITransitoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransitoService implements ITransitoService {

    public final TransitoRepository repo;
    public final PersonaRepository persoRepo;
    public final IPersonaService persoService;

    public TransitoService(TransitoRepository repo,
                           PersonaRepository persoRepo,
                           IPersonaService persoService) {
        this.repo = repo;
        this.persoRepo = persoRepo;
        this.persoService = persoService;
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
    //aca debe traer su id si o si si existe como persona
    public Transito nuevo(Transito transito) {
        Optional<Persona> oPerso=this.persoRepo.findByDni(transito.getDni());
        if(oPerso.isPresent()){
            this.persoService.validarEmailUnico(transito.getEmail());
            transito.setId(oPerso.get().getId());
            this.repo.saveTransito(transito.getId());
            return transito;
        }
        return this.repo.save(transito);
    }
}
