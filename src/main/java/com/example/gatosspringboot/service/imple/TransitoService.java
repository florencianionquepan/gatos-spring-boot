package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.TransitoRepository;
import com.example.gatosspringboot.service.interfaces.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransitoService implements ITransitoService {

    public final TransitoRepository repo;
    public final PersonaRepository persoRepo;
    public final IPersonaService persoService;
    public final IUsuarioService userService;
    private final INotificacionService notiSer;
    private final IEmailService emailSer;

    public TransitoService(TransitoRepository repo,
                           PersonaRepository persoRepo,
                           IPersonaService persoService,
                           IUsuarioService userService,
                           INotificacionService notiSer,
                           IEmailService emailSer) {
        this.repo = repo;
        this.persoRepo = persoRepo;
        this.persoService = persoService;
        this.userService = userService;
        this.notiSer = notiSer;
        this.emailSer = emailSer;
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
//        List<Gato> gatos=anterior.getListaGatos();
//        //aca ver si cambia de transito se lo saco de su listado??
//        gatos.remove(gato);
//        anterior.setListaGatos(gatos);
//        this.notiSer.desasignacionTransito(gato, anterior);
//        return this.repo.save(anterior);
        this.notiSer.desasignacionTransito(gato, anterior);
        return null;
    }

    @Override
    public HashMap<LocalDate,Gato> listarAsignacionesGatos(String email) {
        Optional<Transito> oTran=this.repo.findByEmail(email);
        if(oTran.isEmpty()){
            throw new NonExistingException(
                    String.format("El usuario con email %s no existe",email)
            );
        }
        return oTran.get().getAsignacionesGatos().stream()
                .collect(Collectors.toMap(
                        GatoTransito::getFechaAsociacion, // Clave del mapa: Fecha de asociación
                        GatoTransito::getGato,           // Valor del mapa: Gato asignado
                        (existing, replacement) -> existing, // En caso de claves duplicadas, mantener el valor existente
                        HashMap::new                      // Tipo de mapa a utilizar
                ));
    }

    @Override
    public void notificarAdopcion(Transito transito, Gato gato) {
        Notificacion noti=this.notiSer.notificarAdopcion(transito,gato);
        String subject=gato.getNombre()+" fue adoptado!";
        this.emailSer.armarEnviarEmail(transito.getPersona().getEmail(),subject,noti.getDescripcion());
    }

    //si ya existe con otro dni o email no prosigue-
    private void TransitoExistente(Transito transito){
        Optional<Persona> oPersoDB=this.persoRepo.findByDni(transito.getPersona().getDni());
        if(oPersoDB.isPresent()){
            throw new ExistingException(
                    String.format("El transito con dni %s ya existe",transito.getPersona().getDni()));
        }
        Optional<Persona> oPersoEmailDB=this.persoRepo.findByEmail(transito.getPersona().getEmail());
        if(oPersoEmailDB.isPresent()){
            throw new ExistingException(
                    String.format("El transito con email %s ya existe",transito.getPersona().getEmail()));
        }
    }
}
