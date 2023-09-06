package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.SolicitudVoluntariadoRepository;
import com.example.gatosspringboot.service.interfaces.*;
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
    private final IPersonaService persoService;
    private final IEstadoService estadoService;
    private final ISocioService socioService;
    private final IVoluntarioService voluService;
    private final ITransitoService transitoService;
    private Logger logger= LoggerFactory.getLogger(SolicitudVoluntariadoService.class);

    public SolicitudVoluntariadoService(SolicitudVoluntariadoRepository repo,
                                        PersonaRepository persoRepo,
                                        PersonaService persoService,
                                        IEstadoService estadoService,
                                        ISocioService socioService,
                                        IVoluntarioService voluService,
                                        ITransitoService transitoService) {
        this.repo = repo;
        this.persoRepo = persoRepo;
        this.persoService = persoService;
        this.estadoService = estadoService;
        this.socioService = socioService;
        this.voluService = voluService;
        this.transitoService = transitoService;
    }

    @Override
    public SolicitudVoluntariado nueva(SolicitudVoluntariado solicitud) {
        Persona aspirantebd=this.persoService.findByEmailOrException(solicitud.getAspirante().getEmail());
        //tiene solicitudes de voluntariados del mismo tipo?
        List<SolicitudVoluntariado> voluntariados=aspirantebd.getSolicitudesVoluntariados();
        Optional<SolicitudVoluntariado> solicitudExistente = voluntariados.stream()
                .filter(v -> v.getTipoVoluntariado() == solicitud.getTipoVoluntariado())
                .findFirst();
        if(solicitudExistente.isPresent()){
            this.mismaSolicitudExistente(solicitudExistente.get());
            //si paso 1 mes de su solicitud rechazada sigue:
        }
        Estado pendiente=this.estadoService.crearPendiente();
        solicitud.setAspirante(aspirantebd);
        solicitud.setEstados(new ArrayList<>(Arrays.asList(pendiente)));
        return this.repo.save(solicitud);
    }

    @Override
    @Transactional
    public SolicitudVoluntariado rechazar(SolicitudVoluntariado solicitud, Long id, String motivo) {
        SolicitudVoluntariado soli=this.findById(id);
        String email=solicitud.getSocio().getEmail();
        Socio socio=this.socioService.buscarByEmailOrException(email);
        List<Estado> estados=soli.getEstados();
        estados.add(this.estadoService.crearRechazado(motivo));
        soli.setEstados(estados);
        soli.setSocio(socio);
        //enviar email a solicitante con motivo
        return this.repo.save(soli);
    }

    @Override
    @Transactional
    public SolicitudVoluntariado aceptar(SolicitudVoluntariado solicitud, Long id, String motivo) {
        SolicitudVoluntariado solidb=this.findById(id);
        List<Estado> estados = solidb.getEstados();
        Estado aceptado = estadoService.crearAprobado(motivo);
        estados.add(aceptado);
        solidb.setEstados(estados);
        String emailSocio=solicitud.getSocio().getEmail();
        Socio socio=this.socioService.buscarByEmailOrException(emailSocio);
        solidb.setSocio(socio);
        this.crearTipoVoluntariado(solidb);
        return this.repo.save(solidb);
    }

    private void crearTipoVoluntariado(SolicitudVoluntariado solicitud){
        TipoVoluntariado voluntariado=solicitud.getTipoVoluntariado();
        Persona perso=solicitud.getAspirante();
        if(voluntariado == TipoVoluntariado.VOLUNTARIO){
            Voluntario vol=new Voluntario();
            vol.setId(perso.getId());
            vol.setDni(perso.getDni());
            vol.setNombre(perso.getNombre());
            vol.setApellido(perso.getApellido());
            vol.setFechaNac(perso.getFechaNac());
            vol.setTel(perso.getTel());
            vol.setDire(perso.getDire());
            vol.setLocalidad(perso.getLocalidad());
            vol.setSolicitudesAdopcion(perso.getSolicitudesAdopcion());
            vol.setSolicitudesVoluntariados(perso.getSolicitudesVoluntariados());
            vol.setUsuario(perso.getUsuario());
            vol.setListaGatos(null);
            this.voluService.altaVolunt(vol);
            //enviar email notificando aceptacion (en alta usuario esta hecho)
        } else if (voluntariado == TipoVoluntariado.TRANSITO) {
            Transito transito=new Transito(perso.getId(),perso.getDni(),perso.getNombre(),perso.getApellido(),
                    perso.getTel(),perso.getUsuario().getEmail(),perso.getFechaNac(),perso.getDire(),perso.getLocalidad(),
                    perso.getSolicitudesAdopcion(),perso.getSolicitudesVoluntariados(),perso.getUsuario(),null);
            this.transitoService.nuevo(transito);
            //enviar email notificando aceptacion
        }else{
            throw new NonExistingException
                    (String.format("No tenemos esta clase de voluntariado",voluntariado));
        }
    }

    private SolicitudVoluntariado findById(Long id){
        Optional<SolicitudVoluntariado> oSoli=this.repo.findById(id);
        if(oSoli.isEmpty()){
            throw new NonExistingException(
                    String.format("La solicitud %d no existe",id)
            );
        }
        return oSoli.get();
    }

    //---------Funciones privadas de nueva solicitud-----------

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
            if(mesesTranscurridos<1){
                throw new ExistingException(
                        String.format("La ultima solicitud como %s fue RECHAZADA." +
                                        "Debe esperar 1 mes para volver a enviar una solicitud.",
                                solicitudExistente.getTipoVoluntariado().name())
                );
            }
        }
    }

    private SolicitudVoluntariado nuevaSolicitudPersona(SolicitudVoluntariado solicitud, Persona perso){
        Estado pendiente=this.estadoService.crearPendiente();
        solicitud.setAspirante(perso);
        solicitud.setEstados(new ArrayList<>(Arrays.asList(pendiente)));
        return this.repo.save(solicitud);
    }

    //----------------Listar------------------

    @Override
    public List<SolicitudVoluntariado> listarByEstado(String estado) {
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
    public List<SolicitudVoluntariado> listarByPersona(String dni) {
        Optional<Persona> oPerso=this.persoRepo.findByDni(dni);
        if(oPerso.isEmpty()){
            throw new NonExistingException(
                    String.format("La persona con dni %s no existe",dni)
            );
        }
        return this.repo.findByAspirante(dni);
    }

    @Override
    public List<SolicitudVoluntariado> listarTodas() {
        return (List<SolicitudVoluntariado>) this.repo.findAll();
    }
}
