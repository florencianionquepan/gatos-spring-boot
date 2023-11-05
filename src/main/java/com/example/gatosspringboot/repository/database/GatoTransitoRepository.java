package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.GatoTransito;
import com.example.gatosspringboot.model.Transito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GatoTransitoRepository extends CrudRepository<GatoTransito,Long> {

    @Query("SELECT gt FROM GatoTransito gt WHERE gt.gato = ?1 AND gt.transito = ?2 ORDER BY gt.fechaAsociacion DESC")
    GatoTransito findByGatoAndTransito(Gato gato, Transito transito);

    List<GatoTransito> findByTransito(Transito transito);

    List<GatoTransito> findByGato(Gato gato);
}
