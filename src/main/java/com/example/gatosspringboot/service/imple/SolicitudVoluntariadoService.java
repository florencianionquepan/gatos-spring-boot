package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.SolicitudVoluntariadoRepository;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import com.example.gatosspringboot.service.interfaces.ISolicitudVoluntariadoService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class SolicitudVoluntariadoService implements ISolicitudVoluntariadoService {

    private final SolicitudVoluntariadoRepository repo;
    private final PersonaRepository persoRepo;
    private final IEstadoService estadoService;

    public SolicitudVoluntariadoService(SolicitudVoluntariadoRepository repo,
                                        PersonaRepository persoRepo,
                                        IEstadoService estadoService) {
        this.repo = repo;
        this.persoRepo = persoRepo;
        this.estadoService = estadoService;
    }

    @Override
    public SolicitudVoluntariado nueva(SolicitudVoluntariado solicitud) {
        Optional<Persona> oPerso=this.persoRepo.findByDni(solicitud.getAspirante().getDni());
        //aca puede actualizar su tel, dire, localidad
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

    private void mismaSolicitudExistente(SolicitudVoluntariado solicitudExistente){
        List<Estado> estados=solicitudExistente.getEstados();
        boolean contienePendiente = estados.stream().anyMatch(e -> e.getEstado() == EstadoNombre.PENDIENTE);
        boolean contieneRechazado = estados.stream().anyMatch(e -> e.getEstado() == EstadoNombre.RECHAZADA);
        boolean contieneAceptado = estados.stream().anyMatch(e -> e.getEstado() == EstadoNombre.APROBADA);
        if(contienePendiente && !contieneRechazado && !contieneAceptado){
            throw new ExistingException(
                    String.format("Usted tiene una solicitud para este voluntariado %s" +
                                    "en estado PENDIENTE",
                            solicitudExistente.getTipoVoluntariado().name())
            );
        }else if(contieneAceptado){
            throw new ExistingException(
                    String.format("Usted tiene la solicitud para este voluntariado %s" +
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
                        String.format("La ultima solicitud para este voluntariado %s fue RECHAZADA." +
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
