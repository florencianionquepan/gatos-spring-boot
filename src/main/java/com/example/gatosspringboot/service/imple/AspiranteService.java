package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Aspirante;
import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.Socio;
import com.example.gatosspringboot.repository.database.AspiranteRepository;
import com.example.gatosspringboot.service.interfaces.IAspiranteService;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AspiranteService implements IAspiranteService {

    private final AspiranteRepository repo;
    private final IEstadoService estadoService;

    public AspiranteService(AspiranteRepository repo,
                            IEstadoService estadoService) {
        this.repo = repo;
        this.estadoService = estadoService;
    }

    @Override
    public Aspirante altaAspirante(Aspirante aspirante) {
        Estado pendiente=this.estadoService.crearPendiente();
        aspirante.setEstados(new ArrayList<>(Arrays.asList(pendiente)));
        return this.repo.save(aspirante);
    }

    @Override
    //aca debo traer el socio que lo acepto
    //se crea el voluntario/transito
    public Aspirante aceptarAspirante(Aspirante aspirante, Long id) {
        return null;
    }

    @Override
    //aca debo traer el socio que lo rechazo
    public Aspirante rechazarAspirante(Aspirante aspirante, Long id) {
        return null;
    }

    @Override
    public List<Aspirante> listarTodos() {
        return (List<Aspirante>) this.repo.findAll();
    }

    @Override
    public List<Aspirante> listarByEstado(Estado estado) {
        return null;
    }

    @Override
    public List<Aspirante> listarBySocio(Socio socio) {
        return null;
    }
}
