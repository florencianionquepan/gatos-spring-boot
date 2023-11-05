package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.CuotaRepository;
import com.example.gatosspringboot.repository.database.GatoRepository;
import com.example.gatosspringboot.repository.database.PadrinoRepository;
import com.example.gatosspringboot.service.interfaces.IEmailService;
import com.example.gatosspringboot.service.interfaces.INotificacionService;
import com.example.gatosspringboot.service.interfaces.IPadrinoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PadrinoService implements IPadrinoService {

    private final PadrinoRepository repo;
    private final INotificacionService notiService;
    private final IEmailService emailService;
    private final GatoRepository gatoRepo;
    private final CuotaRepository cuotaRepo;

    public PadrinoService(PadrinoRepository repo,
                          INotificacionService notiService,
                          IEmailService emailService,
                          GatoRepository gatoRepo,
                          CuotaRepository cuotaRepo) {
        this.repo = repo;
        this.notiService = notiService;
        this.emailService = emailService;
        this.gatoRepo = gatoRepo;
        this.cuotaRepo = cuotaRepo;
    }

    @Override
    public Padrino buscarByEmailOrException(String email) {
        Optional<Padrino> oPadri=this.repo.buscarByEmail(email);
        if(oPadri.isEmpty()){
            throw new NonExistingException(
                    String.format("El padrino con email %s no existe",
                            email)
            );
        }
        return oPadri.get();
    }

    @Override
    public void notificarAdopcion(Padrino padrino, Gato gato) {
        Notificacion noti=this.notiService.notificarAdopcion(padrino,gato);
        String subject=gato.getNombre()+" fue adoptado!";
        this.emailService.armarEnviarEmail(padrino.getPersona().getEmail(),subject,noti.getDescripcion());
    }

    @Override
    public Padrino removerGato(String email, Gato gato) {
        Padrino padri=this.buscarByEmailOrException(email);
        Optional<Gato> oGato=this.gatoRepo.findById(gato.getId());
        if(oGato.isEmpty()){
            throw new NonExistingException("El gato no existe");
        }
        oGato.get().setPadrino(null);
        this.gatoRepo.save(oGato.get());
        padri.getListaGatos().remove(oGato.get());
        //las cuotas de ese gatito que no estaban aprobadas cerrarlas
        this.cerrarCuotasGatoNoAprobadas(padri,gato);
        return this.repo.save(padri);
    }

    @Override
    public Padrino revisarCuotasImpagas(Long id) {
        Optional<Padrino> oPadri=this.repo.findById(id);
        if(oPadri.isEmpty()){
            throw new NonExistingException("El padrino no existe");
        }
        Padrino padrino=oPadri.get();
        LocalDate actual=LocalDate.now();
        List<Cuota> cuotasImpagas = padrino.getListaCuotas().stream()
                .filter(cuota -> cuota.getEstadoPago() == EstadoPago.RECHAZADO || cuota.getEstadoPago() == EstadoPago.DESCONOCIDO || cuota.getEstadoPago() == EstadoPago.PENDIENTE)
                .filter(cuota -> cuota.getEstadoPago() == EstadoPago.PENDIENTE ? cuota.getFechaCreacion().isBefore(actual.minusDays(10)) : cuota.getFechaCreacion().isBefore(actual.minusDays(7)))
                .collect(Collectors.toList());
        if(!cuotasImpagas.isEmpty()){
            cuotasImpagas.forEach(cuota->{
                this.removerGato(oPadri.get().getPersona().getEmail(), cuota.getGato());
            });
        }
        return padrino;
    }

    private void cerrarCuotasGatoNoAprobadas(Padrino padri, Gato gato) {
        List<Cuota> cuotas=padri.getListaCuotas().stream()
                .filter(cuota-> cuota.getGato().getId()==gato.getId())
                .filter(cuota->cuota.getEstadoPago()!= EstadoPago.APROBADO)
                .collect(Collectors.toList());
        for(Cuota cuota:cuotas){
            cuota.setEstadoPago(EstadoPago.CENCELADO);
            this.cuotaRepo.save(cuota);
        }
    }
}
