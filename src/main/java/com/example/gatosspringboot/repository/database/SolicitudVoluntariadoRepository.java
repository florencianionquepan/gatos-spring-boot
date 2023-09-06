package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.EstadoNombre;
import com.example.gatosspringboot.model.SolicitudVoluntariado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SolicitudVoluntariadoRepository extends CrudRepository<SolicitudVoluntariado,Long> {

    @Query("SELECT s FROM SolicitudVoluntariado s JOIN s.estados e WHERE e.estado = ?1")
    List<SolicitudVoluntariado> findByEstado(EstadoNombre nombre);

    @Query("SELECT s FROM SolicitudVoluntariado s WHERE s.aspirante.dni = ?1")
    List<SolicitudVoluntariado> findByAspirante(String dni);
}
