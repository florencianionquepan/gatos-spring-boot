package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.EstadoNombre;
import com.example.gatosspringboot.model.SolicitudAdopcion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends CrudRepository<SolicitudAdopcion,Long> {

    @Query("SELECT s FROM Solicitud s JOIN s.estados e WHERE e.estado = ?1")
    List<SolicitudAdopcion> findByEstado(EstadoNombre nombre);

    @Query("SELECT s FROM Solicitud s WHERE s.solicitante.dni = ?1")
    List<SolicitudAdopcion> findBySolicitante(String dni);

    @Query("SELECT s FROM Solicitud s WHERE s.gato.id = ?1")
    List<SolicitudAdopcion> findByGato(Long id);

/*    @Query("select s from Solicitud s where s.fechaSolicitud>=?1 and s.fechaSolicitud<=?2")
    List<Solicitud> findBetweenDates(LocalDate desde, LocalDate hasta);*/
}
