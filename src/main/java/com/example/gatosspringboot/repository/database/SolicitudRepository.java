package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.EstadoNombre;
import com.example.gatosspringboot.model.Solicitud;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolicitudRepository extends CrudRepository<Solicitud,Long> {

    @Query("SELECT s FROM Solicitud s JOIN s.estados e WHERE e.estado = ?1")
    List<Solicitud> findByEstado(EstadoNombre nombre);

    @Query("SELECT s FROM Solicitud s WHERE s.solicitante.dni = ?1")
    List<Solicitud> findBySolicitante(String dni);

    @Query("SELECT s FROM Solicitud s WHERE s.gato.id = ?1")
    List<Solicitud> findByGato(Long id);

    @Query("select s from Solicitud s where s.fechaSolicitud>=?1 and s.fechaSolicitud<=?2")
    List<Solicitud> findBetweenDates(LocalDate desde, LocalDate hasta);
}
