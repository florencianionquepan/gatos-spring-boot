package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Socio;
import com.example.gatosspringboot.repository.database.SocioRepository;
import com.example.gatosspringboot.service.interfaces.ISocioService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocioService implements ISocioService {

    private final SocioRepository repo;

    public SocioService(SocioRepository repo) {
        this.repo = repo;
    }

    @Override
    public Socio buscarByEmail(String email) {
        Optional<Socio> oSocio=this.repo.findByEmail(email);
        if(oSocio.isEmpty()){
            throw new NonExistingException(
                    String.format("El socio con email %d no existe",email));
        }
        return oSocio.get();
    }
}
