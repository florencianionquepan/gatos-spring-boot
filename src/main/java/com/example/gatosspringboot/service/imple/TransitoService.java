package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.GatoTransitoRepository;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.TransitoRepository;
import com.example.gatosspringboot.service.interfaces.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class TransitoService implements ITransitoService {

    public final TransitoRepository repo;
    public final PersonaRepository persoRepo;
    public final IPersonaService persoService;
    public final IUsuarioService userService;
    private final INotificacionService notiSer;
    private final IEmailService emailSer;
    private final GatoTransitoRepository asignacionRepo;

    public TransitoService(TransitoRepository repo,
                           PersonaRepository persoRepo,
                           IPersonaService persoService,
                           IUsuarioService userService,
                           INotificacionService notiSer,
                           IEmailService emailSer,
                           GatoTransitoRepository asignacionRepo) {
        this.repo = repo;
        this.persoRepo = persoRepo;
        this.persoService = persoService;
        this.userService = userService;
        this.notiSer = notiSer;
        this.emailSer = emailSer;
        this.asignacionRepo = asignacionRepo;
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
        Optional<Persona> oPerso=this.persoRepo.findByDni(transito.getPersona().getDni());
        //siempre va a existir porqie ahora debe registrarse primero pero igual lo dejo
        //por si a futuro implemento el alta directa
        if(oPerso.isPresent()){
            return this.repo.save(transito);
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

    @Override
    public Transito addGato(GatoTransito asociacion) {
        Transito nuevoTran=asociacion.getTransito();
        List<GatoTransito> gatos=nuevoTran.getAsignacionesGatos();
        gatos.add(asociacion);
        nuevoTran.setAsignacionesGatos(gatos);
        this.notiSer.asignacionTransito(asociacion.getGato(),nuevoTran);
        return this.repo.save(nuevoTran);
    }

    @Override
    public Transito notificarTransitoAnterior(Gato gato, Transito anterior) {
        GatoTransito asign=this.asignacionRepo.findByGatoAndTransito(gato,anterior);
        LocalDate actual=LocalDate.now();
        asign.setFechaFin(actual);
        this.asignacionRepo.save(asign);
        this.notiSer.desasignacionTransito(gato, anterior);
        return null;
    }

    @Override
    public List<GatoTransito> listarAsignacionesGatos(String email) {
        Optional<Transito> oTran=this.repo.findByEmail(email);
        if(oTran.isEmpty()){
            throw new NonExistingException(
                    String.format("El usuario con email %s no existe",email)
            );
        }
        return this.asignacionRepo.findByTransito(oTran.get());
    }

    @Override
    public void notificarAdopcion(Transito transito, Gato gato) {
        GatoTransito asignacion=this.asignacionRepo.findByGatoAndTransito(gato,transito);
        LocalDate fechaFin=LocalDate.now();
        asignacion.setFechaFin(fechaFin);
        this.asignacionRepo.save(asignacion);
        Notificacion noti=this.notiSer.notificarAdopcion(transito,gato);
        String subject=gato.getNombre()+" fue adoptado!";
        this.emailSer.armarEnviarEmail(transito.getPersona().getEmail(),subject,noti.getDescripcion());
    }

    //si ya existe ese transito con otro dni o email no prosigue-
    private void TransitoExistente(Transito transito){
        Optional<Transito> oTrans=this.repo.findByDni(transito.getPersona().getDni());
        if(oTrans.isPresent()){
            throw new ExistingException(
                    String.format("El transito con dni %s ya existe",transito.getPersona().getDni()));
        }
        Optional<Transito> oTransEmail=this.repo.findByEmail(transito.getPersona().getEmail());
        if(oTransEmail.isPresent()){
            throw new ExistingException(
                    String.format("El transito con email %s ya existe",transito.getPersona().getEmail()));
        }
    }
}
