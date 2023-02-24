package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Voluntario;
import com.example.gatosspringboot.repository.database.GatoRepository;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import com.example.gatosspringboot.service.interfaces.IVoluntarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GatoService implements IGatoService {

    private final GatoRepository gatoRepo;
    private final IVoluntarioService voluService;

    public GatoService(GatoRepository gatoRepo, IVoluntarioService voluService) {
        this.gatoRepo = gatoRepo;
        this.voluService = voluService;
    }

    @Override
    public List<Gato> verTodos() {
        return (List<Gato>) this.gatoRepo.findAll();
    }

    @Override
    public Gato altaGato(Gato gato) {
        if(!this.voluService.existeVol(gato.getVoluntario().getId())){
            return null;
        }
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
        Voluntario vol=gato.getVoluntario();
        Voluntario volBD=this.voluService.verTodos().stream()
                .filter(v->v.getId()==vol.getId()).findAny().get();
        List<Gato> gatitosVol=volBD.getListaGatos();
        gatitosVol.add(gato);
        volBD.setListaGatos(gatitosVol);
    }

    private void removeGatoVolViejo(Long id){
        Gato gatoAnte=this.buscarPorId(id);
        if(gatoAnte.getVoluntario()!=null){
            Voluntario voluntAnte=gatoAnte.getVoluntario();
            List<Gato> gatosVolAnte=voluntAnte.getListaGatos();
            gatosVolAnte.remove(gatoAnte);
        }
    }

    private Gato buscarPorId(Long id){
        return this.gatoRepo.findById(id).get();
    }

    public boolean existeGato(Long id){
        return this.gatoRepo.existsById(id);
    }
}
