package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.SolicitudRepository;
import com.example.gatosspringboot.service.interfaces.*;
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
    private final INotificacionService notiService;
    private final IEmailService emailService;
    private Logger logger= LoggerFactory.getLogger(SolicitudService.class);

    public SolicitudService(SolicitudRepository repo,
                            IEstadoService estadoService,
                            IGatoService gatoService,
                            IPersonaService persoService,
                            INotificacionService notiService,
                            IEmailService emailService) {
        this.repo = repo;
        this.estadoService = estadoService;
        this.gatoService = gatoService;
        this.persoService = persoService;
        this.notiService = notiService;
        this.emailService = emailService;
    }

    @Override
    public List<SolicitudAdopcion> verSolicitudes() {
        return (List<SolicitudAdopcion>) this.repo.findAll();
    }

    @Override
    public List<SolicitudAdopcion> verByEstado(String estado) {
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
    public List<SolicitudAdopcion> verByGato(Long idGato) {
        return this.repo.findByGato(idGato);
    }

    @Override
    public List<SolicitudAdopcion> verBySolicitante(String email) {
        return this.repo.findBySolicitanteEmail(email);
    }

    @Override
    public List<SolicitudAdopcion> verAceptadasBySolicitante(String dni) {
        List<SolicitudAdopcion> solicitudesPorSolicitante = this.repo.findBySolicitante(dni);
        List<SolicitudAdopcion> solicitudesAceptadas = new ArrayList<>();
        for (SolicitudAdopcion solicitud : solicitudesPorSolicitante) {
            for (Estado estado : solicitud.getEstados()) {
                if (estado.getEstado() == EstadoNombre.APROBADA) {
                    solicitudesAceptadas.add(solicitud);
                    break; // Salir del bucle si se encuentra una solicitud aceptada
                }
            }
        }
        return solicitudesAceptadas;
    }

    @Override
    public List<SolicitudAdopcion> verRangoFechas(LocalDate desde, LocalDate hasta) {
        return (List<SolicitudAdopcion>) this.repo.findAll();
    }

    @Override
    //que filtre las que tengan como ultimo estado pendiente
    public List<SolicitudAdopcion> verByGatoPendientes(Long idGato) {
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
    //puede tener hasta 2 pendientes
    public SolicitudAdopcion altaSolicitud(SolicitudAdopcion solicitudAdopcion) {
        //buscar solicitante y gato:
        Persona solicitantebd=this.persoService.findByEmailOrException(solicitudAdopcion.getSolicitante().getEmail());
        Gato gatobd=this.gatoService.buscarDisponibleById(solicitudAdopcion.getGato().getId());
        //chequear que no haya hecho ya una solicitud por el mismo gato
        this.solicitaMismoGato(solicitantebd, gatobd);
        this.poseeOtrosPendientes(solicitantebd);
        Estado pendiente=estadoService.crearPendiente();
        List<Estado> estados = new ArrayList<>();
        estados.add(pendiente);
        solicitudAdopcion.setEstados(estados);
        //logger.info("solicitud= "+solicitud);
        solicitudAdopcion.setSolicitante(solicitantebd);
        solicitudAdopcion.setGato(gatobd);
        //crear notificacion al voluntario del gato
        this.notiService.nuevaSolicitudAdopcion(gatobd);
        return this.repo.save(solicitudAdopcion);
    }

    //solo se puede enviar si se cerro porque el gato se adopto
    // y volvio a estar en adopcion
    private void solicitaMismoGato(Persona solicitante, Gato gato){
        List<SolicitudAdopcion> solicitudesAnteriores=solicitante.getSolicitudesAdopcion();
        Optional<SolicitudAdopcion> solicitudEncontrada = solicitudesAnteriores.stream()
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
        List<SolicitudAdopcion> solicitudes=soli.getSolicitudesAdopcion();
        LocalDate fechaActual = LocalDate.now();
        LocalDate hace30Dias = fechaActual.minusDays(30);
        List<SolicitudAdopcion> solicitudesFiltradas = solicitudes.stream()
                .filter(solicitud -> solicitud.getEstados().size() == 1 &&
                        solicitud.getEstados().get(0).getEstado() == EstadoNombre.PENDIENTE &&
                        solicitud.getEstados().get(0).getFecha().isAfter(hace30Dias))
                .collect(Collectors.toList());
        if(solicitudesFiltradas.size()>1){
            throw new ExistingException("Ya solicitaste la adopcion de dos gatitos en los ultimos 30 dias," +
                    "por favor espera a que revisemos las solicitudes enviadas!");
        };
    }

    //--------------Aceptar solicitud de adopcion :)-------------------

    @Override
    public SolicitudAdopcion aceptarAdopcion(Long id, String motivo) {
        SolicitudAdopcion solidb=this.findByIdOrException(id);
        Gato gatoAdoptar= solidb.getGato();
        //chequear que el gato no este adoptado. gatoService lo chequea
        Gato gatoAdoptado=gatoService.adoptarGato(gatoAdoptar.getId());
        SolicitudAdopcion actualizada=this.addEstadoAprobado(solidb, motivo);
        //enviar email que adoptaste al gati y noti
        this.enviarEmailAprobada(solidb);
        this.notiService.aprobacionAdopcion(gatoAdoptar,solidb.getSolicitante());
        //cerrar todas las demas solicitudes del gato
        this.cerrarPendientes(gatoAdoptar, solidb);
        return this.repo.save(actualizada);
    }

    private void cerrarPendientes(Gato gatoAdoptar, SolicitudAdopcion solicitudAdopcionActual) {
        List<SolicitudAdopcion> pendientes=this.verByGatoPendientes(gatoAdoptar.getId()).stream()
                .filter(solicitud -> !solicitud.getId().equals(solicitudAdopcionActual.getId()))
                .collect(Collectors.toList());
        for(SolicitudAdopcion solicitudAdopcion :pendientes){
            Estado cerrado=this.estadoService.crearCerrado();
            List<Estado> estados= solicitudAdopcion.getEstados();
            estados.add(cerrado);
            solicitudAdopcion.setEstados(estados);
            //crear notificaciones
            Notificacion noti=this.notiService.cierreAdopcion(gatoAdoptar,solicitudAdopcion.getSolicitante());
            this.repo.save(solicitudAdopcion);
        }
    }

    @Override
    public SolicitudAdopcion rechazarSolicitud(Long id, String motivo) {
        SolicitudAdopcion solidb=this.findByIdOrException(id);
        SolicitudAdopcion actualizada=this.addEstadoRechazada(solidb,motivo);
        //enviar email de rechazo y noti
        this.enviarEmailRechazada(solidb);
        this.notiService.rechazoAdopcion(solidb.getGato(),solidb.getSolicitante());
        return this.repo.save(actualizada);
    }

    private SolicitudAdopcion findByIdOrException(Long id){
        Optional<SolicitudAdopcion> oSoli=this.repo.findById(id);
        if(oSoli.isEmpty()){
            throw new NonExistingException(
                    String.format("La solicitud %d no existe",id));
        }
        return oSoli.get();
    }

    private SolicitudAdopcion addEstadoAprobado(SolicitudAdopcion solidb, String motivo){
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

    private void enviarEmailAprobada(SolicitudAdopcion solidb){
        String gato=solidb.getGato().getNombre();
        String soliNombre=solidb.getSolicitante().getNombre();
        String subject="Novedades en solicitud de adopcion";
        String texto="Hola "+soliNombre+"! Nos complace contarte que hemos aceptado tu solicitud para adoptar a "+gato+"!";
        this.emailService.armarEnviarEmail(solidb.getSolicitante().getEmail(), subject,texto);
    }

    private SolicitudAdopcion addEstadoRechazada(SolicitudAdopcion solidb, String motivo){
        //chequear que el estado no este rechazada ya
        List<Estado> estados=solidb.getEstados();
        Optional<Estado> oRechazada=estados.stream()
                .filter(e->e.getEstado().equals(EstadoNombre.RECHAZADA))
                .findAny();
        if(oRechazada.isPresent()){
            throw new RuntimeException("La solicitud ya fue rechazada");
        }
        Estado aprobado=estadoService.crearRechazado(motivo);
        estados.add(aprobado);
        solidb.setEstados(estados);
        return solidb;
    }

    private void enviarEmailRechazada(SolicitudAdopcion solidb){
        String gato=solidb.getGato().getNombre();
        String soliNombre=solidb.getSolicitante().getNombre();
        String subject="Novedades en solicitud de adopcion";
        String texto="Hola "+soliNombre+"! Lamentamos comentarte que tu solicitud para adoptar a "+gato+" fue rechazada.";
        this.emailService.armarEnviarEmail(solidb.getSolicitante().getEmail(), subject,texto);
    }
}
