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
import java.util.*;
import java.util.stream.Collectors;

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
            throw new NonExistingException("El estado proporcionado no es válido.");
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
    public List<Solicitud> verRangoFechas(LocalDate desde, LocalDate hasta) {
        return (List<Solicitud>) this.repo.findAll();
    }

    @Override
    public List<Solicitud> verByGatoPendientes(Long idGato) {
        return this.repo.findByGato(idGato).stream()
                .filter(s->s.getEstados().stream()
                        .anyMatch(e->e.getEstado().equals("PENDIENTE")))
                .collect(Collectors.toList());
    }

    @Override
    //puede tener hasta 3 pendientes
    public Solicitud altaSolicitud(Solicitud solicitud) {
        //buscar solicitante y gato:
        Persona solicitantebd=this.persoService.findByEmail(solicitud.getSolicitante().getEmail());
        Gato gatobd=this.gatoService.buscarDisponibleById(solicitud.getGato().getId());
        //chequear que no haya hecho ya una solicitud por el mismo gato
        this.solicitaMismoGato(solicitantebd, gatobd);
        this.poseeOtrosPendientes(solicitantebd);
        Estado pendiente=estadoService.crearPendiente();
        List<Estado> estados = new ArrayList<>();
        estados.add(pendiente);
        solicitud.setEstados(estados);
        //logger.info("solicitud= "+solicitud);
        solicitud.setSolicitante(solicitantebd);
        solicitud.setGato(gatobd);
        //this.persoService.addSolicitudPersona(solicitud);
        //this.gatoService.addSolicitudGato(solicitud);
        return this.repo.save(solicitud);
    }

    //solo se puede enviar si se cerro porque el gato se adopto
    // y volvio a estar en adopcion
    private void solicitaMismoGato(Persona solicitante, Gato gato){
        List<Solicitud> solicitudesAnteriores=solicitante.getSolicitudesAdopcion();
        Optional<Solicitud> solicitudEncontrada = solicitudesAnteriores.stream()
                .filter(soli -> soli.getGato().getId().equals(gato.getId()))
                .findFirst();
        if(solicitudEncontrada.isPresent()){
            List<Estado> estados=solicitudEncontrada.get().getEstados();
            boolean existeEstadoCerrado = estados.stream()
                    .anyMatch(estado -> estado.getEstado() == EstadoNombre.CERRADA);
            //ya sea que esta pendiente o se reviso y se rechazo o acepto
            if(!existeEstadoCerrado){
                throw new ExistingException(
                        String.format("Ya enviaste una solicitud por la adopcion de este gatito!")
                );
            }
        };
    }

    private void poseeOtrosPendientes(Persona soli){
        List<Solicitud> solicitudes=soli.getSolicitudesAdopcion();
        LocalDate fechaActual = LocalDate.now();
        LocalDate hace30Dias = fechaActual.minusDays(30);
        List<Solicitud> solicitudesFiltradas = solicitudes.stream()
                .filter(solicitud -> solicitud.getEstados().size() == 1 &&
                        solicitud.getEstados().get(0).getEstado() == EstadoNombre.PENDIENTE &&
                        solicitud.getEstados().get(0).getFecha().isAfter(hace30Dias))
                .collect(Collectors.toList());
        if(solicitudesFiltradas.size()>2){
            throw new ExistingException("Ya solicitaste la adopcion de dos gatitos en los ultimos 30 dias," +
                    "por favor espera a que revisemos las solicitudes enviadas!");
        };
    }

    @Override
    public Solicitud aceptarAdopcion(Solicitud solicitud, Long id) {
        this.existeSolicitud(id);
        Gato gatoAdoptar= solicitud.getGato();
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
