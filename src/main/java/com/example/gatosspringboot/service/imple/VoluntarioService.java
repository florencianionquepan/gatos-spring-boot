package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
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
            throw new RuntimeException(
                    String.format("El dni %d ya existe"
                    ,vol.getDni())
            );
        }
        return this.voluRepo.save(vol);
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
