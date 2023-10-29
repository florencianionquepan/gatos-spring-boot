package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
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

import java.time.LocalDate;
import java.util.ArrayList;
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
    public String creacionPreferencia(Cuota cuota) {
        Optional<Gato> oGato=this.gatoRepo.findById(cuota.getGato().getId());
        if(oGato.isEmpty()){
            throw new PersonNotFound(
                    String.format("El gato con id %d no existe",cuota.getGato().getId())
            );
        }
        cuota.setGato(oGato.get());
        Optional<Padrino> oPadri=this.padriRepo.buscarByEmail(cuota.getPadrino().getPersona().getEmail());
        if(oPadri.isEmpty()){
            Optional<Persona> oPerso=this.persoRepo.findByEmail(cuota.getPadrino().getPersona().getEmail());
            if(oPerso.isEmpty()){
                throw new NonExistingException("Debe registrarse para esta accion");
            }else {
                Persona perso = oPerso.get();
                Padrino nuevo = new Padrino(0L, perso, new ArrayList<>(), new ArrayList<>());
                Padrino padrino = this.padriRepo.save(nuevo);
                cuota.setPadrino(padrino);
                //oGato.get().setPadrino(padrino);
                //this.gatoRepo.save(oGato.get());
            }
        }else{
            Padrino padrino=oPadri.get();
            cuota.setPadrino(padrino);
            //oGato.get().setPadrino(padrino);
            //this.gatoRepo.save(oGato.get());
        }
        LocalDate fecha=LocalDate.now();
        cuota.setFechaPago(fecha);
        Cuota nueva=this.repo.save(cuota);
        String response;
        try{
            response=this.MPservice.crearPreferencia(nueva);
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    @Transactional
    public Cuota modiCuota(String preferenciaId) {
        Cuota cuota=this.repo.findByPreferencia_id(preferenciaId);
        cuota.setPagada(true);
        Cuota modi=this.repo.save(cuota);
        return modi;
    }
}
