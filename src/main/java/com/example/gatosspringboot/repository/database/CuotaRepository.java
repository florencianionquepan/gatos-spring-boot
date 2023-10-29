package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Cuota;
import org.springframework.data.repository.CrudRepository;

public interface CuotaRepository extends CrudRepository<Cuota,Long> {

    Cuota findByPreferencia_id(String preferencia_id);
}
