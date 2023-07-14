package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.SolicitudVoluntariadoRepository;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import com.example.gatosspringboot.service.interfaces.ISocioService;
import com.example.gatosspringboot.service.interfaces.ISolicitudVoluntariadoService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class SolicitudVoluntariadoService implements ISolicitudVoluntariadoService {

    private final SolicitudVoluntariadoRepository repo;
    private final PersonaRepository persoRepo;
    private final IEstadoService estadoService;
    private final ISocioService socioService;
    private Logger logger= LoggerFactory.getLogger(SolicitudVoluntariadoService.class);

    public SolicitudVoluntariadoService(SolicitudVoluntariadoRepository repo,
                                        PersonaRepository persoRepo,
                                        IEstadoService estadoService,
                                        ISocioService socioService) {
        this.repo = repo;
        this.persoRepo = persoRepo;
        this.estadoService = estadoService;
        this.socioService = socioService;
    }

    @Override
    public SolicitudVoluntariado nueva(SolicitudVoluntariado solicitud) {
        Optional<Persona> oPerso=this.persoRepo.findByDni(solicitud.getAspirante().getDni());
        if(oPerso.isPresent()){
            Persona perso=oPerso.get();
            //tiene solicitudes de voluntariados del mismo tipo?
            List<SolicitudVoluntariado> voluntariados=perso.getSolicitudesVoluntariados();
            Optional<SolicitudVoluntariado> solicitudExistente = voluntariados.stream()
                    .filter(v -> v.getTipoVoluntariado() == solicitud.getTipoVoluntariado())
                    .findFirst();
            if(solicitudExistente.isPresent()){
                this.mismaSolicitudExistente(solicitudExistente.get());
                //si pasaron 3 meses de su solicitud rechazada:
                return nuevaSolicitudPersona(solicitud,perso);
            }
            return nuevaSolicitudPersona(solicitud,perso);
        }
        //controlar que el email no sea existente
        this.mailExistente(solicitud.getAspirante());
        return crearEstadoYSave(solicitud,solicitud.getAspirante());
    }

    @Override
    @Transactional
    public SolicitudVoluntariado rechazar(SolicitudVoluntariado solicitud, Long id, String motivo) {
        Optional<SolicitudVoluntariado> oSoli=this.repo.findById(id);
        if(oSoli.isEmpty()){
            throw new NonExistingException(
                    String.format("La solicitud %d no existe",id)
            );
        }
        SolicitudVoluntariado soli=oSoli.get();
        String email=solicitud.getSocio().getEmail();
        Socio socio=this.socioService.buscarByEmail(email);
        List<Estado> estados=soli.getEstados();
        estados.add(this.estadoService.crearRechazado(motivo));
        soli.setEstados(estados);
        soli.setSocio(socio);
        return this.repo.save(soli);
    }

    @Override
    public SolicitudVoluntariado aceptar(SolicitudVoluntariado solicitud, Long id) {
        return null;
    }

    private void mismaSolicitudExistente(SolicitudVoluntariado solicitudExistente){
        List<Estado> estados=solicitudExistente.getEstados();
        boolean contienePendiente = estados.stream().anyMatch(e -> e.getEstado() == EstadoNombre.PENDIENTE);
        boolean contieneRechazado = estados.stream().anyMatch(e -> e.getEstado() == EstadoNombre.RECHAZADA);
        boolean contieneAceptado = estados.stream().anyMatch(e -> e.getEstado() == EstadoNombre.APROBADA);
        if(contienePendiente && !contieneRechazado && !contieneAceptado){
            throw new ExistingException(
                    String.format("Usted tiene una solicitud para %s " +
                                    "en estado PENDIENTE",
                            solicitudExistente.getTipoVoluntariado().name())
            );
        }else if(contieneAceptado){
            throw new ExistingException(
                    String.format("Usted tiene la solicitud para %s " +
                                    "en estado ACEPTADO",
                            solicitudExistente.getTipoVoluntariado().name())
            );
        }else if(contieneRechazado){
            Optional<Estado> ultimoRechazado = estados.stream()
                    .filter(e -> e.getEstado() == EstadoNombre.RECHAZADA)
                    .sorted(Comparator.comparing(Estado::getFecha).reversed())
                    .findFirst();
            LocalDate ultimoRechazo=ultimoRechazado.get().getFecha();
            LocalDate fechaHoy=LocalDate.now();
            long mesesTranscurridos = ChronoUnit.MONTHS.between(ultimoRechazo, fechaHoy);
            if(mesesTranscurridos<3){
                throw new ExistingException(
                        String.format("La ultima solicitud como %s fue RECHAZADA." +
                                        "Debe esperar 3 meses para volver a enviar una solicitud.",
                                solicitudExistente.getTipoVoluntariado().name())
                );
            }
        }
    }

    private SolicitudVoluntariado nuevaSolicitudPersona(SolicitudVoluntariado solicitud, Persona perso){
        //crear estado pendiente, nueva solicitud,actualizar tel, dire, localidad.
        perso.setTel(solicitud.getAspirante().getTel());
        perso.setDire(solicitud.getAspirante().getDire());
        perso.setLocalidad(solicitud.getAspirante().getLocalidad());
        return crearEstadoYSave(solicitud,perso);
    }

    @Transactional
    private SolicitudVoluntariado crearEstadoYSave(SolicitudVoluntariado solicitud,
                                                     Persona perso){
        Estado pendiente=this.estadoService.crearPendiente();
        Persona per=this.persoRepo.save(perso);
        solicitud.setAspirante(per);
        solicitud.setEstados(new ArrayList<>(Arrays.asList(pendiente)));
        return this.repo.save(solicitud);
    }

    private void mailExistente(Persona perso){
        Optional<Persona> oPersoEmail=this.persoRepo.findByEmail(perso.getEmail());
        if(oPersoEmail.isPresent()){
            throw new ExistingException(
                    String.format("El email %s ya existe y corresponde a otra persona",
                            oPersoEmail.get().getEmail())
            );
        }
    }

    @Override
    public List<SolicitudVoluntariado> listarByEstado() {
        return null;
    }

    @Override
    public List<SolicitudVoluntariado> listarByPersona(String dni) {
        return null;
    }

    @Override
    public List<SolicitudVoluntariado> listarTodas() {
        return null;
    }
}
