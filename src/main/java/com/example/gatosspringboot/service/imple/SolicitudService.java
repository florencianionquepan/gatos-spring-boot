package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.SolicitudRepository;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.example.gatosspringboot.service.interfaces.ISolicitudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService implements ISolicitudService {

    private final SolicitudRepository repo;
    private final IEstadoService estadoService;
    private final IGatoService gatoService;
    private final IPersonaService persoService;
    private Logger logger= LoggerFactory.getLogger(SolicitudService.class);

    public SolicitudService(SolicitudRepository repo,
                            IEstadoService estadoService,
                            IGatoService gatoService,
                            IPersonaService persoService) {
        this.repo = repo;
        this.estadoService = estadoService;
        this.gatoService = gatoService;
        this.persoService = persoService;
    }

    @Override
    public List<Solicitud> verSolicitudes() {
        return (List<Solicitud>) this.repo.findAll();
    }

    @Override
    public List<Solicitud> verByEstado(String estado) {
        EstadoNombre nombre=EstadoNombre.valueOf(estado.toUpperCase());
        if (nombre==EstadoNombre.PENDIENTE ||
                nombre==EstadoNombre.APROBADA ||
                nombre==EstadoNombre.RECHAZADA) {
            return this.repo.findByEstado(nombre);
        } else {
            throw new IllegalArgumentException("El estado proporcionado no es válido.");
        }
    }


    @Override
    public List<Solicitud> verByGato(Long idGato) {
        return this.repo.findByGato(idGato);
    }

    @Override
    public List<Solicitud> verBySolicitante(String dni) {
        return this.repo.findBySolicitante(dni);
    }

    @Override
    public Solicitud altaSolicitud(Solicitud solicitud) {
        //chequear primero que no haya hecho ya una solicitud por el mismo gato
        this.solicitaMismoGato(solicitud);
        Estado pendiente=estadoService.crearPendiente();
        List<Estado> estados = new ArrayList<>();
        estados.add(pendiente);
        solicitud.setEstados(estados);
        LocalDate fechaHoy=LocalDate.now();
        solicitud.setFechaSolicitud(fechaHoy);
        //logger.info("solicitud= "+solicitud);
        //si la persona no existe tambien la crea
        this.persoService.addSolicitudPersona(solicitud);
        this.gatoService.addSolicitudGato(solicitud);
        return this.repo.save(solicitud);
    }

    private void solicitaMismoGato(Solicitud solicitud){
        String dniSolicita=solicitud.getSolicitante().getDni();
        boolean existeSolicitante=this.persoService.existeByDni(dniSolicita);
        if(existeSolicitante){
            Persona solicitante=this.persoService.findByDni(dniSolicita);
            Long idGatoSolicitado=solicitud.getGato().getId();
            List<Solicitud> solicitudesAnteriores=solicitante.getSolicitudes();
            if(solicitudesAnteriores.stream()
                    .anyMatch(soli -> soli.getGato().getId().equals(idGatoSolicitado))){
                throw new ExistingException(
                        String.format("Ya enviaste una solicitud por la adopcion de este gatito!")
                );
            };
        }
    }

    @Override
    public Solicitud aceptarAdopcion(Solicitud solicitud, Long id) {
        this.existeSolicitud(id);
        Gato gatoAdoptar=solicitud.getGato();
        //chequear que el gato no este adoptado. gatoService lo chequea
        Gato gatoAdoptado=gatoService.adoptarGato(gatoAdoptar.getId());
        Solicitud actualizada=this.addEstadoSolicitud(id);
        return this.repo.save(actualizada);
    }

    @Override
    public Solicitud rechazarSolicitud(Solicitud solicitud, Long id) {
        return null;
    }

    private boolean existeSolicitud(Long id){
        boolean existe=this.repo.existsById(id);
        if(!existe){
            throw new NonExistingException(
                    String.format("La solicitud %d no existe",id));
        }
        return existe;
    }

    private Solicitud addEstadoSolicitud(Long id){
        //chequear que el estado no este aprobado ya
        Optional<Solicitud> oSoli=this.repo.findById(id);
        List<Estado> estados=oSoli.get().getEstados();
        Optional<Estado> oAprobado=estados.stream()
                .filter(e->e.getEstado().equals(EstadoNombre.APROBADA))
                .findAny();
        if(oAprobado.isPresent()){
            throw new RuntimeException("La solicitud ya fue aprobada");
        }
        Estado aprobado=estadoService.crearAprobado();
        estados.add(aprobado);
        oSoli.get().setEstados(estados);
        return oSoli.get();
    }
}
