package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.Ficha;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Transito;
import com.example.gatosspringboot.model.Voluntario;
import com.example.gatosspringboot.repository.database.FichaRepository;
import com.example.gatosspringboot.repository.database.GatoRepository;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import com.example.gatosspringboot.service.interfaces.ITransitoService;
import com.example.gatosspringboot.service.interfaces.IVoluntarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class GatoService implements IGatoService {

    private final GatoRepository gatoRepo;
    private final IVoluntarioService voluService;
    private final FichaRepository fichaRepo;
    private final ITransitoService tranSer;

    public GatoService(GatoRepository gatoRepo,
                       IVoluntarioService voluService,
                       FichaRepository fichaRepo,
                       ITransitoService tranSer) {
        this.gatoRepo = gatoRepo;
        this.voluService = voluService;
        this.fichaRepo = fichaRepo;
        this.tranSer = tranSer;
    }

    @Override
    public List<Gato> verTodos() {
        return (List<Gato>) this.gatoRepo.findAll();
    }

    @Override
    public Gato verById(Long id) {
        Optional<Gato> oGato=this.gatoRepo.findById(id);
        if(oGato.isEmpty()){
            throw new NonExistingException(
                    String.format("El gato con id %d no existe",id)
            );
        }
        return oGato.get();
    }

    @Override
    public Gato altaGato(Gato gato) {
        Voluntario volGato=this.voluService.buscarVolByEmailOrException(gato.getVoluntario().getPersona().getEmail());
        gato.setVoluntario(volGato);
        this.addGatoVol(gato);
        //queda tambien avisar al padrino y transito
        return this.gatoRepo.save(gato);
    }

    @Override
    public Gato modiGato(Gato gato, Long id) {
        this.removeGatoVolViejo(id);
        //queda tambien avisar al padrino y transito
        gato.setId(id);
        this.addGatoVol(gato);
        return this.gatoRepo.save(gato);
    }

    private void addGatoVol(Gato gato){
        Voluntario volGato=this.voluService.buscarVolByEmailOrException(gato.getVoluntario().getPersona().getEmail());
        List<Gato> gatitosVol=volGato.getListaGatos();
        gatitosVol.add(gato);
        volGato.setListaGatos(gatitosVol);
    }

    //Para cuando actualizas el voluntario del gato
    private void removeGatoVolViejo(Long id){
        Gato gatoAnte=this.findGatoById(id);
        if(gatoAnte.getVoluntario()!=null){
            Voluntario voluntAnte=gatoAnte.getVoluntario();
            List<Gato> gatosVolAnte=voluntAnte.getListaGatos();
            gatosVolAnte.remove(gatoAnte);
            voluntAnte.setListaGatos(gatosVolAnte);
        }
    }

    public boolean existeGato(Long id){
        return this.gatoRepo.existsById(id);
    }

    @Override
    public Gato adoptarGato(Long id) {
        Gato gati=this.buscarDisponibleById(id);
        gati.setAdoptadoFecha(LocalDate.now());
        //notificarPadrino
        //notificarTransito
        return this.gatoRepo.save(gati);
    }

    @Override
    public Gato buscarDisponibleById(Long id) {
        Optional<Gato> oGato=this.gatoRepo.findById(id);
        if(oGato.isEmpty()){
            throw new PersonNotFound(
                    String.format("El gato con id %d no existe",id)
            );
        }
        LocalDate fechaAdopcion=oGato.get().getAdoptadoFecha();
        if(fechaAdopcion!=null){
            DateTimeFormatter formatoFecha=DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault());
            String fechaFormateada = fechaAdopcion.format(formatoFecha);
            throw new PersonNotFound(
                    String.format("El gato fue adoptado el día %s",fechaFormateada)
            );
        }
        return oGato.get();
    }

    @Override
    public Gato agregarFicha(Ficha ficha, Long id) {
        Gato gati=this.findGatoById(id);
        Ficha creada=this.fichaRepo.save(ficha);
        gati.setFichaVet(creada);
        return this.gatoRepo.save(gati);
    }

    @Override
    //si tenia un transito anterior se va a pisar
    public Gato agregarTransito(Transito transito, Long id) {
        Gato gati=this.findGatoById(id);
        Transito transitodb=this.tranSer.findByIdOrException(transito.getId());
        gati.setTransito(transitodb);
        return this.gatoRepo.save(gati);
    }

    private Gato findGatoById(Long id){
        Optional<Gato> oGato=this.gatoRepo.findById(id);
        if(oGato.isEmpty()){
            throw new PersonNotFound(
                    String.format("El gato con id %d no existe",id)
            );
        }
        return oGato.get();
    }
}
