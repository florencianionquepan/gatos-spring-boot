package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Transito;
import com.example.gatosspringboot.repository.database.TransitoRepository;
import com.example.gatosspringboot.service.interfaces.ITransitoService;

import java.util.List;

public class TransitoService implements ITransitoService {

    public final TransitoRepository repo;

    public TransitoService(TransitoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Transito> listarTodos() {
        return null;
    }

    @Override
    public List<Transito> listarByLocalidad(String localidad) {
        return null;
    }

    @Override
    public Transito nuevo(Transito transito) {
        return null;
    }
}
