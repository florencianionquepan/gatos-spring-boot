package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.EstadoNombre;
import com.example.gatosspringboot.repository.database.EstadoRepository;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EstadoService implements IEstadoService {

    private EstadoRepository repo;

    @Override
    public Estado estadoPendiente(Estado estado) {
        LocalDate fecha=LocalDate.now();
        estado.setFecha(fecha);
        estado.setEstado(EstadoNombre.PENDIENTE);
        return this.repo.save(estado);
    }

    @Override
    public Estado estadoAprobado(Estado estado, Long id) {
        LocalDate fecha=LocalDate.now();
        estado.setFecha(fecha);
        estado.setEstado(EstadoNombre.APROBADA);
        return this.repo.save(estado);
    }

    @Override
    public Estado estadoRechazado(Estado estado, Long id) {
        LocalDate fecha=LocalDate.now();
        estado.setFecha(fecha);
        estado.setEstado(EstadoNombre.RECHAZADA);
        return this.repo.save(estado);
    }
}
