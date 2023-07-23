package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Estado;
import com.example.gatosspringboot.model.EstadoNombre;
import com.example.gatosspringboot.repository.database.EstadoRepository;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EstadoService implements IEstadoService {

    private final EstadoRepository repo;

    public EstadoService(EstadoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Estado crearPendiente() {
        Estado pendiente=new Estado();
        LocalDate fecha=LocalDate.now();
        pendiente.setFecha(fecha);
        pendiente.setEstado(EstadoNombre.PENDIENTE);
        return this.repo.save(pendiente);
    }

    @Override
    public Estado crearAprobado(String motivo) {
        Estado aprobado=new Estado();
        LocalDate fecha=LocalDate.now();
        aprobado.setFecha(fecha);
        aprobado.setEstado(EstadoNombre.APROBADA);
        aprobado.setMotivo(motivo);
        return this.repo.save(aprobado);
    }

    @Override
    public Estado crearRechazado(String motivo) {
        Estado rechazado=new Estado();
        LocalDate fecha=LocalDate.now();
        rechazado.setFecha(fecha);
        rechazado.setEstado(EstadoNombre.RECHAZADA);
        rechazado.setMotivo(motivo);
        return this.repo.save(rechazado);
    }

    @Override
    public Estado crearCerrado() {
        Estado cerrado=new Estado();
        LocalDate fecha=LocalDate.now();
        cerrado.setFecha(fecha);
        cerrado.setEstado(EstadoNombre.CERRADA);
        return this.repo.save(cerrado);
    }

    private Estado buscarById(Long id){
        Optional<Estado> oEstado=this.repo.findById(id);
        if(oEstado.isEmpty()){
            throw new NonExistingException("El estado no existe");
        }
        return oEstado.get();
    }
}
