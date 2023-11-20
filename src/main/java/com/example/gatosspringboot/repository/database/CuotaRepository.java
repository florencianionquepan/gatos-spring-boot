package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.EstadoPago;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CuotaRepository extends CrudRepository<Cuota,Long> {

    @Query("SELECT c FROM Cuota c WHERE c.preferencia_id = ?1")
    Cuota findByPreferencia_id(String preferencia_id);

    @Query("from Cuota c WHERE c.padrino.persona.email = ?1")
    List<Cuota> listarByPadrino(String email);

    List<Cuota> findAllByEstadoPago(EstadoPago estadoPago);
}
