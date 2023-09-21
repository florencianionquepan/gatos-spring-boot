package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.controller.GatoController;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GatoService implements IGatoService {

    private final GatoRepository gatoRepo;
    private final IVoluntarioService voluService;
    private final FichaRepository fichaRepo;
    private final ITransitoService tranSer;
    private final ICloudinaryService cloudService;
    private final FotoRepository fotoRepo;

    private Logger logger= LoggerFactory.getLogger(GatoService.class);

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
        List<Foto> fotosGatitos=this.guardarFotos(fotos);
        gato.setFotos(fotosGatitos);
        return this.gatoRepo.save(gato);
    }

    @Override
    @Transactional
    public Gato modiGato(Gato gato,MultipartFile[] files, Long id) {
        Voluntario volGato=this.voluService.buscarVolByEmailOrException(gato.getVoluntario().getPersona().getEmail());
        //List<Foto> todasLasFotos = new ArrayList<>();
        List<Foto> fotosGatitos =null;
        if (Arrays.stream(files).count()==0L) {
            fotosGatitos = this.guardarFotos(files);
            //todasLasFotos.addAll(fotosGatitos);
        }
        this.eliminarFotos(gato, id);
        gato.setVoluntario(volGato);
        //luego vuelvo a buscar las fotos que quedaron
        Gato gatodb=this.findGatoById(id);
        gato.setId(id);
        List<Foto> fotos=gatodb.getFotos();
        if(fotosGatitos!=null){
            fotos.addAll(fotosGatitos);
        }
        gato.setFotos(fotos);
        return this.gatoRepo.save(gato);
    }

    private void eliminarFotos(Gato gatoMod, Long id){
        Gato gatodb=this.findGatoById(id);
        List<Foto> fotosDb = gatodb.getFotos();
        List<Foto> fotosModificadas = gatoMod.getFotos();
        List<Foto> fotosAEliminar = fotosDb.stream()
                .filter(fotoDb -> fotosModificadas.stream()
                        .noneMatch(fotoModificada -> fotoModificada.getFotoUrl().equals(fotoDb.getFotoUrl())))
                .collect(Collectors.toList());
        //logger.info("eliminar="+fotosAEliminar);
        for(Foto foto:fotosAEliminar){
            this.fotoRepo.deleteByFotoUrl(foto.getFotoUrl());
        }
    }

    private List<Foto> guardarFotos(MultipartFile[] files){
        List<Foto> fotosGatitos=new ArrayList<>();
        try {
            List<Map> results=this.cloudService.upload(files);
            for(Map result:results){
                Foto foto=new Foto(0L, (String) result.get("original_filename"),
                        (String) result.get("url"), (String) result.get("public_id"), null);
                Foto nueva=this.fotoRepo.save(foto);
                fotosGatitos.add(nueva);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fotosGatitos;
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

    //or exception
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
