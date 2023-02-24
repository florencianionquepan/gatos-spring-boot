package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Voluntario;
import com.example.gatosspringboot.repository.database.VoluntarioRepository;
import com.example.gatosspringboot.service.interfaces.IVoluntarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoluntarioService implements IVoluntarioService {

    private final VoluntarioRepository voluRepo;

    public VoluntarioService(VoluntarioRepository voluRepo) {
        this.voluRepo = voluRepo;
    }

    @Override
    public List<Voluntario> verTodos() {
        return (List<Voluntario>) this.voluRepo.findAll();
    }

    @Override
    public Voluntario altaVolunt(Voluntario vol) {
        if(this.existeDni(vol.getDni())){
            return null;
        }
        return this.voluRepo.save(vol);
    }

    @Override
    public Voluntario modiVolunt(Voluntario vol, Long id) {
        if(!this.voluRepo.existsById(id)){
            return null;
        }
        if(this.existeDni(vol.getDni())){
            return null;
        }
        vol.setId(id);
        return this.voluRepo.save(vol);
    }

    @Override
    public boolean existeVol(Long id) {
        return this.voluRepo.existsById(id);
    }

    private boolean existeDni(String dni){
        boolean existe=false;
        Optional<Voluntario> oVolu=this.voluRepo.findByDni(dni);
        if(oVolu.isPresent()){
            existe=true;
        }
        return existe;
    }

}
