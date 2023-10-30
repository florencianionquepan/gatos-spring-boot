package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.*;
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
        Cuota nueva=this.creacionCuota(cuota);
        String response;
        try{
            response=this.MPservice.crearPreferencia(nueva);
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    //este metodo se va a ejecutar cuando pasen 30 dias de cuota aprobada tambien
    public Cuota creacionCuota(Cuota cuota) {
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
                //le seteamos al gato el padrino nuevo:
                oGato.get().setPadrino(padrino);
                this.gatoRepo.save(oGato.get());
            }
        }else{
            //si el padrino ya es padrino
            Padrino padrino=oPadri.get();
            cuota.setPadrino(padrino);
            //si el padrino no es de este gato
            if(!padrino.getListaGatos().contains(oGato.get())){
                oGato.get().setPadrino(padrino);
                this.gatoRepo.save(oGato.get());
            }
        }
        LocalDate fecha=LocalDate.now();
        cuota.setFechaCreacion(fecha);
        return this.repo.save(cuota);
    }

    @Override
    @Transactional
    public Cuota pagoCuotaAprobado(String preferenciaId) {
        Cuota cuota=this.repo.findByPreferencia_id(preferenciaId);
        LocalDate actual=LocalDate.now();
        cuota.setFechaPago(actual);
        cuota.setEstadoPago(EstadoPago.APROBADO);
        Cuota modi=this.repo.save(cuota);
        return modi;
    }

    @Override
    public Cuota pagoCuotaRechazado(String preferenciaId) {
        Cuota cuota=this.repo.findByPreferencia_id(preferenciaId);
        cuota.setEstadoPago(EstadoPago.RECHAZADO);
        Cuota modi=this.repo.save(cuota);
        return modi;
    }
}
