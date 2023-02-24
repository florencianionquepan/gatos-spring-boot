package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Gato;
import org.springframework.data.repository.CrudRepository;

public interface GatoRepository extends CrudRepository<Gato,Long> {
}
