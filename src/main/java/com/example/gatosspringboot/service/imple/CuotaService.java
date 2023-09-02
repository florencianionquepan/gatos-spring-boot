package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.repository.database.CuotaRepository;
import com.example.gatosspringboot.repository.database.GatoRepository;
import com.example.gatosspringboot.repository.database.PadrinoRepository;
import com.example.gatosspringboot.service.interfaces.ICuotaService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CuotaService implements ICuotaService {

    private final CuotaRepository repo;
    private final GatoRepository gatoRepo;
    private final PadrinoRepository padriRepo;

    public CuotaService(CuotaRepository repo,
                        GatoRepository gatoRepo,
                        PadrinoRepository padriRepo) {
        this.repo = repo;
        this.gatoRepo = gatoRepo;
        this.padriRepo = padriRepo;
    }

    @Override
    public Cuota nuevaCuota(Cuota cuota) {
        Optional<Gato> oGato=this.gatoRepo.findById(cuota.getGato().getId());
        if(oGato.isEmpty()){
            throw new PersonNotFound(
                    String.format("El gato con id %d no existe",cuota.getGato().getId())
            );
        }
        Optional<Padrino> oPadri=this.padriRepo.findByEmail(cuota.getPadrino().getEmail());
        if(oPadri.isEmpty()){
            throw new PersonNotFound(
                    String.format("El padrino con email %s no existe",cuota.getPadrino().getEmail())
            );
        }
        //cuota.getMontoMensual();


        return null;
    }
}
