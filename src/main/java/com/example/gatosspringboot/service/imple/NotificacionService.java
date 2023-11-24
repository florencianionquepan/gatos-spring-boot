package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.NotificacionRepository;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.service.interfaces.IEmailService;
import com.example.gatosspringboot.service.interfaces.INotificacionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService implements INotificacionService {

    private final NotificacionRepository repo;
    private final PersonaRepository persorepo;
    private final IEmailService emailSer;

    public NotificacionService(NotificacionRepository repo,
                               PersonaRepository persorepo,
                               IEmailService emailSer) {
        this.repo = repo;
        this.persorepo = persorepo;
        this.emailSer = emailSer;
    }

    @Override
    public Notificacion nuevaSolicitudAdopcion(Gato gato) {
        Notificacion nueva=new Notificacion();
        Voluntario volu=gato.getVoluntario();
        nueva.setDescripcion(gato.getNombre()+" recibio una nueva solicitud para ser adoptado.");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(volu.getPersona());
        nueva.setPath("/backoffice/misgatos");
        return this.repo.save(nueva);
    }

    @Override
    //al propio transito
    public Notificacion asignacionTransito(Gato gato, Transito transito) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Gracias por brindarle a "+gato.getNombre()+
                " un hogar temporal. Ahora, "+gato.getNombre()+" forma parte de tu listado!");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(transito.getPersona());
        nueva.setPath("/backoffice/gatosentransito");
        return this.repo.save(nueva);
    }

    @Override
    //al padrino
    public Notificacion asignacionTransito(Gato gato, Padrino padrino) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion(gato.getNombre()+" esta en un nuevo hogar temporal!." +
                "Accede a su perfil para mas información.");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(padrino.getPersona());
        nueva.setPath("/gatos/"+gato.getId());
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion desasignacionTransito(Gato gato, Transito transitoAnterior) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion(gato.getNombre()+" ya no forma parte de tu transito.");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(transitoAnterior.getPersona());
        nueva.setPath("/backoffice/gatosentransito");
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion rechazoAdopcion(Gato gato, Persona solicitante) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Tu solicitud por: "+gato.getNombre()+" fue rechazada!");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(solicitante);
        nueva.setPath("/backoffice/missolicitudes");
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion aprobacionAdopcion(Gato gato, Persona solicitante) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Nos alegra contarte que adoptaste a "+gato.getNombre()+
                "!. Un voluntario se estara comunicando para coordinar el traspaso :)");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(solicitante);
        nueva.setPath("/backoffice/missolicitudes");
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion notificarAdopcion(Transito transito, Gato gato) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Tu transito "+gato.getNombre()+
                " fue adoptado!. Un voluntario se estara comunicando para coordinar el traspaso :)");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(transito.getPersona());
        nueva.setPath("/backoffice/gatosentransito");
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion notificarAdopcion(Padrino padrino, Gato gato) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Queremos notificarte que "+gato.getNombre()+
                " fue adoptado!.Podés seguir apadrinando otros michis!");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(padrino.getPersona());
        nueva.setPath(" ");
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion cierreAdopcion(Gato gato, Persona solicitante) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Tu solicitud de adopcion por "+gato.getNombre()+
                " ha sido cerrada. El voluntario aceptó otro solicitante.");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(solicitante);
        nueva.setPath("/backoffice/missolicitudes");
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion actualizacionCuota(Padrino padrino, Gato gato) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Se actualizo la cuota mensual de "+gato.getNombre()+".");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(padrino.getPersona());
        nueva.setPath("/backoffice/miscuotas");
        return this.repo.save(nueva);
    }

    @Override
    public List<Notificacion> verByPersona(String email) {
        Optional<Persona> oPerso=this.persorepo.findByEmail(email);
        if(oPerso.isEmpty()){
            throw new NonExistingException(
                    String.format("La persona con email %s no existe",email)
            );
        }
        return this.repo.findAllByPersona(oPerso.get());
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

    @Override
    public Notificacion rechazoVoluntariado(Persona aspirante, TipoVoluntariado tipo) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Tu solicitud para ser "+tipo.name().toLowerCase()+" ha sido rechazada." +
                "Accede a tus solicitudes para mas información");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(aspirante);
        nueva.setPath("/backoffice/missolicitudes");
        this.emailSer.armarEnviarEmail(aspirante.getEmail(), "Rechazo voluntariado",nueva.getDescripcion());
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion aceptarVoluntariado(Persona aspirante, TipoVoluntariado tipo) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Bienvenido a Rescats "+aspirante.getNombre() +
                "!. Ahora eres uno de nuestros "+tipo.name().toLowerCase()+"s!");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(aspirante);
        nueva.setPath("/backoffice/perfil");
        this.emailSer.armarEnviarEmail(aspirante.getEmail(), "Bienvenido a Rescats!",nueva.getDescripcion());
        return this.repo.save(nueva);
    }

    @Override
    public Notificacion nuevoRolSocio(Persona persona) {
        Notificacion nueva=new Notificacion();
        nueva.setDescripcion("Su usuario tiene ahora permisos de administrador!");
        LocalDate fecha=LocalDate.now();
        nueva.setFechaCreacion(fecha);
        nueva.setPersona(persona);
        nueva.setPath("/backoffice/usuarios");
        this.emailSer.armarEnviarEmail(persona.getEmail(), "Nuevos permisos en su cuenta de Rescats!"
                ,nueva.getDescripcion());
        return this.repo.save(nueva);
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
