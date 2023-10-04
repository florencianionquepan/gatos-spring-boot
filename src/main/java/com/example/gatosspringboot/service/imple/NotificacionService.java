package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Notificacion;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Voluntario;
import com.example.gatosspringboot.repository.database.NotificacionRepository;
import com.example.gatosspringboot.service.interfaces.INotificacionService;
import com.example.gatosspringboot.service.interfaces.IPersonaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService implements INotificacionService {

    private final NotificacionRepository repo;
    private final IPersonaService persoSer;

    public NotificacionService(NotificacionRepository repo,
                               IPersonaService persoSer) {
        this.repo = repo;
        this.persoSer = persoSer;
    }

    @Override
    public Notificacion nuevaSolicitudAdopcion(Gato gato) {
        Notificacion nueva=new Notificacion();
        Voluntario volu=gato.getVoluntario();
        nueva.setDescripcion("El gato "+gato.getNombre()+" recibio una nueva solicitud para ser adoptado");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(volu.getPersona());
        return this.repo.save(nueva);
    }

    @Override
    public List<Notificacion> verByPersona(String email) {
        Persona perso=this.persoSer.findByEmailOrException(email);
        return this.repo.findAllByPersona(perso);
    }

    @Override
    public List<Notificacion> setearComoLeidas(List<Notificacion> notificaciones) {
        List<Notificacion> notiLeidas=new ArrayList<>();
        for(Notificacion noti:notificaciones){
            Notificacion notidb=this.findByIdOrException(noti);
            notidb.setLeida(true);
            Notificacion leida=this.repo.save(notidb);
            notiLeidas.add(leida);
        }
        return notiLeidas;
    }

    private Notificacion findByIdOrException(Notificacion noti){
        Optional<Notificacion> oNoti =this.repo.findById(noti.getId());
        if(oNoti.isEmpty()){
            throw new NonExistingException(
                    String.format("La notificacion con id %d no existe",noti.getId())
            );
        }
        return oNoti.get();
    }
}
