package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.model.Voluntario;
import com.example.gatosspringboot.repository.database.PadrinoRepository;
import com.example.gatosspringboot.service.interfaces.IPadrinoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PadrinoService implements IPadrinoService {

    public final PadrinoRepository repo;

    public PadrinoService(PadrinoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Padrino buscarByEmailOrException(String email) {
        Optional<Padrino> oPadri=this.repo.buscarByEmail(email);
        if(oPadri.isEmpty()){
            throw new NonExistingException(
                    String.format("El padrino con email %s no existe",
                            email)
            );
        }
        return oPadri.get();
    }
}
