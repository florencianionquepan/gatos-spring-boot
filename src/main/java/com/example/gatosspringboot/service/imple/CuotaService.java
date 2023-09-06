package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.repository.database.CuotaRepository;
import com.example.gatosspringboot.repository.database.GatoRepository;
import com.example.gatosspringboot.repository.database.PadrinoRepository;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.service.interfaces.ICuotaService;
import com.example.gatosspringboot.service.interfaces.IMercadoPagoService;
import com.example.gatosspringboot.service.interfaces.IPadrinoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CuotaService implements ICuotaService {

    private final CuotaRepository repo;
    private final GatoRepository gatoRepo;
    private final PersonaRepository persoRepo;
    private final PadrinoRepository padriRepo;
    private final IPadrinoService padriService;
    private final IMercadoPagoService MPservice;
    private Logger logger= LoggerFactory.getLogger(CuotaService.class);

    public CuotaService(CuotaRepository repo,
                        GatoRepository gatoRepo,
                        PersonaRepository persoRepo,
                        PadrinoRepository padriRepo,
                        IPadrinoService padriService,
                        IMercadoPagoService mPservice) {
        this.repo = repo;
        this.gatoRepo = gatoRepo;
        this.persoRepo = persoRepo;
        this.padriRepo = padriRepo;
        this.padriService = padriService;
        MPservice = mPservice;
    }

    @Override
    @Transactional
    public Cuota nuevaCuota(Cuota cuota) {
        Optional<Gato> oGato=this.gatoRepo.findById(cuota.getGato().getId());
        if(oGato.isEmpty()){
            throw new PersonNotFound(
                    String.format("El gato con id %d no existe",cuota.getGato().getId())
            );
        }
        Optional<Padrino> oPadri=this.padriRepo.buscarByEmail(cuota.getPadrino().getEmail());
//        if(oPadri.isEmpty()){
//            Optional<Persona> oPerso=this.persoRepo.findPersonByEmail(cuota.getPadrino().getEmail());
//            Long idPersona=oPerso.get().getId();
//            this.padriRepo.savePadrino(idPersona);
//            Padrino padrino=this.padriService.buscarByEmailOrException(cuota.getPadrino().getEmail());
//            logger.info("Padrino="+padrino);
//            oGato.get().setPadrino(padrino);
//            this.gatoRepo.save(oGato.get());
//        }else{
//            Padrino padrino=oPadri.get();
//            oGato.get().setPadrino(padrino);
//            this.gatoRepo.save(oGato.get());
//        }
        //logger.info("ahora="+oGato.get().getPadrino());
        try{
            this.MPservice.crearPago(oGato.get());
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
