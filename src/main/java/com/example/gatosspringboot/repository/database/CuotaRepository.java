package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Cuota;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CuotaRepository extends CrudRepository<Cuota,Long> {

    @Query("SELECT c FROM Cuota c WHERE c.preferencia_id = ?1")
    Cuota findByPreferencia_id(String preferencia_id);
}
