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
    //que filtre las que tengan como ultimo estado pendiente
    public List<Solicitud> verByGatoPendientes(Long idGato) {
        return this.repo.findByGato(idGato).stream()
                .filter(s-> {
                    Estado ultimo=s.getEstados().stream()
                            .sorted(Comparator.comparingLong(Estado::getId).reversed())
                            .findFirst()
                            .orElse(null);
                    return ultimo!=null && ultimo.getEstado().equals(EstadoNombre.PENDIENTE);
                })
                .collect(Collectors.toList());
    }

    @Override
    //puede tener hasta 3 pendientes
    public Solicitud altaSolicitud(Solicitud solicitud) {
        //buscar solicitante y gato:
        Persona solicitantebd=this.persoService.findByEmailOrException(solicitud.getSolicitante().getEmail());
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

    //--------------Aceptar solicitud de adopcion :)-------------------

    @Override
    public Solicitud aceptarAdopcion(Long id, String motivo) {
        Solicitud solidb=this.findByIdOrException(id);
        Gato gatoAdoptar= solidb.getGato();
        //chequear que el gato no este adoptado. gatoService lo chequea
        Gato gatoAdoptado=gatoService.adoptarGato(gatoAdoptar.getId());
        Solicitud actualizada=this.addEstadoAprobado(solidb, motivo);
        //enviar email que adoptaste al gati
        //cerrar todas las demas solicitudes del gato
        this.cerrarPendientes(gatoAdoptar, solidb);
        return this.repo.save(actualizada);
    }

    private void cerrarPendientes(Gato gatoAdoptar, Solicitud solicitudActual) {
        List<Solicitud> pendientes=this.verByGatoPendientes(gatoAdoptar.getId()).stream()
                .filter(solicitud -> !solicitud.getId().equals(solicitudActual.getId()))
                .collect(Collectors.toList());
        for(Solicitud solicitud:pendientes){
            Estado cerrado=this.estadoService.crearCerrado();
            List<Estado> estados=solicitud.getEstados();
            estados.add(cerrado);
            solicitud.setEstados(estados);
            this.repo.save(solicitud);
        }
    }

    @Override
    public Solicitud rechazarSolicitud(Solicitud solicitud, Long id) {
        return null;
    }

    private Solicitud findByIdOrException(Long id){
        Optional<Solicitud> oSoli=this.repo.findById(id);
        if(oSoli.isEmpty()){
            throw new NonExistingException(
                    String.format("La solicitud %d no existe",id));
        }
        return oSoli.get();
    }

    private Solicitud addEstadoAprobado(Solicitud solidb, String motivo){
        //chequear que el estado no este aprobado ya
        List<Estado> estados=solidb.getEstados();
        Optional<Estado> oAprobado=estados.stream()
                .filter(e->e.getEstado().equals(EstadoNombre.APROBADA))
                .findAny();
        if(oAprobado.isPresent()){
            throw new RuntimeException("La solicitud ya fue aprobada");
        }
        Estado aprobado=estadoService.crearAprobado(motivo);
        estados.add(aprobado);
        solidb.setEstados(estados);
        return solidb;
    }
}
