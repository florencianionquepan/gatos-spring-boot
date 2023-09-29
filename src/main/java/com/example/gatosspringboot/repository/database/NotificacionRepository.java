package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Notificacion;
import com.example.gatosspringboot.model.Persona;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificacionRepository extends CrudRepository<Notificacion, Long> {
    List<Notificacion> findAllByPersona(Persona persona);
}
