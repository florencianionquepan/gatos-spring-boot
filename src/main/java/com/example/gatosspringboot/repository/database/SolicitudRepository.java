package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Solicitud;
import org.springframework.data.repository.CrudRepository;

public interface SolicitudRepository extends CrudRepository<Solicitud,Long> {
}
