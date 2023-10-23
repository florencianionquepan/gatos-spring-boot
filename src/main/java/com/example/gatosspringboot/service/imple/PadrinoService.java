package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Notificacion;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.repository.database.PadrinoRepository;
import com.example.gatosspringboot.service.interfaces.IEmailService;
import com.example.gatosspringboot.service.interfaces.INotificacionService;
import com.example.gatosspringboot.service.interfaces.IPadrinoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PadrinoService implements IPadrinoService {

    private final PadrinoRepository repo;
    private final INotificacionService notiService;
    private final IEmailService emailService;

    public PadrinoService(PadrinoRepository repo,
                          INotificacionService notiService,
                          IEmailService emailService) {
        this.repo = repo;
        this.notiService = notiService;
        this.emailService = emailService;
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
}
