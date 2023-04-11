package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Solicitud;
import com.example.gatosspringboot.repository.database.SolicitudRepository;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import com.example.gatosspringboot.service.interfaces.ISolicitudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService implements ISolicitudService {

    private final SolicitudRepository repo;
    private final IEstadoService estadoService;

    public SolicitudService(SolicitudRepository repo,
                            IEstadoService estadoService) {
        this.repo = repo;
        this.estadoService = estadoService;
    }

    @Override
    public List<Solicitud> verSolicitudes() {
        return (List<Solicitud>) this.repo.findAll();
    }

    @Override
    public Solicitud altaSolicitud(Solicitud solicitud) {
        Estado pendiente=estadoService.crearPendiente();
        solicitud.setEstado(pendiente);
        //ver si a este estado hay que agregarle la solicitud a su lista
        return this.repo.save(solicitud);
    }

    @Override
    public Solicitud aceptarAdopcion(Solicitud solicitud, Long id) {
        this.existeSolicitud(id);
        //chequear que el estado no este aprobado ya
        //chequear que el gato no este adoptado
        Gato gatoAdoptar=solicitud.getGato();
        Estado aprobado=estadoService.estadoAprobado(solicitud.getEstado().getId());
        solicitud.setEstado(aprobado);
        return this.repo.save(solicitud);
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
}
