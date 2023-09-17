package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.FichaRepository;
import com.example.gatosspringboot.repository.database.FotoRepository;
import com.example.gatosspringboot.repository.database.GatoRepository;
import com.example.gatosspringboot.service.interfaces.ICloudinaryService;
import com.example.gatosspringboot.service.interfaces.IGatoService;
import com.example.gatosspringboot.service.interfaces.ITransitoService;
import com.example.gatosspringboot.service.interfaces.IVoluntarioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class GatoService implements IGatoService {

    private final GatoRepository gatoRepo;
    private final IVoluntarioService voluService;
    private final FichaRepository fichaRepo;
    private final ITransitoService tranSer;
    private final ICloudinaryService cloudService;
    private final FotoRepository fotoRepo;

    public GatoService(GatoRepository gatoRepo,
                       IVoluntarioService voluService,
                       FichaRepository fichaRepo,
                       ITransitoService tranSer,
                       ICloudinaryService cloudService,
                       FotoRepository fotoRepo) {
        this.gatoRepo = gatoRepo;
        this.voluService = voluService;
        this.fichaRepo = fichaRepo;
        this.tranSer = tranSer;
        this.cloudService = cloudService;
        this.fotoRepo = fotoRepo;
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
    public List<Gato> verByVoluntario(String email) {
        Voluntario volGato=this.voluService.buscarVolByEmailOrException(email);
        return this.gatoRepo.findByVoluntarioEmail(volGato.getPersona().getEmail());
    }

    @Override
    @Transactional
    public Gato altaGato(Gato gato, MultipartFile[] fotos) {
        Voluntario volGato=this.voluService.buscarVolByEmailOrException(gato.getVoluntario().getPersona().getEmail());
        gato.setVoluntario(volGato);
        this.addGatoVol(gato);
        //queda tambien avisar al padrino y transito-> transito le asigno aparte y padrino se asocia el mismo
        //guardar urls fotos de cloudinary en db y asociarlas al gatito
        List<Foto> fotosGatitos=new ArrayList<>();
        try {
            List<Map> results=this.cloudService.upload(fotos);
            for(Map result:results){
                Foto foto=new Foto(0L, (String) result.get("original_filename"),
                        (String) result.get("url"), (String) result.get("public_id"));
                Foto nueva=this.fotoRepo.save(foto);
                fotosGatitos.add(nueva);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gato.setFotos(fotosGatitos);
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
