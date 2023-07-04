package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.SolicitudRepository;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import com.example.gatosspringboot.service.interfaces.ISolicitudService;
import org.springframework.stereotype.Service;

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
    public Solicitud altaSolicitud(Solicitud solicitud) {
        Estado pendiente=estadoService.crearPendiente();
        solicitud.setEstados(new ArrayList<>(Arrays.asList(pendiente)));
        //si la persona no existe tambien la crea
        this.persoService.addSolicitudPersona(solicitud);
        this.gatoService.addSolicitudGato(solicitud);
        return this.repo.save(solicitud);
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
