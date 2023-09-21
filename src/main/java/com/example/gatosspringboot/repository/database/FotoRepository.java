package com.example.gatosspringboot.repository.database;

import com.example.gatosspringboot.model.Foto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FotoRepository extends CrudRepository<Foto,Long> {

    long deleteByFotoUrl(String fotoUrl);

    List<Foto> findByFotoUrl(String fotoUrl);
}
