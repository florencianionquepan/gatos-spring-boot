package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.FotoRepository;
import com.example.gatosspringboot.repository.database.GatoRepository;
import com.example.gatosspringboot.service.interfaces.*;
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
    private final IFichaService fichaService;
    private final ITransitoService tranSer;
    private final ICloudinaryService cloudService;
    private final FotoRepository fotoRepo;

    private Logger logger= LoggerFactory.getLogger(GatoService.class);

    public GatoService(GatoRepository gatoRepo,
                       IVoluntarioService voluService,
                       IFichaService fichaService,
                       ITransitoService tranSer,
                       ICloudinaryService cloudService,
                       FotoRepository fotoRepo) {
        this.gatoRepo = gatoRepo;
        this.voluService = voluService;
        this.fichaService = fichaService;
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
        Gato nuevo=this.gatoRepo.save(gato);
        List<Foto> fotosGatitos=new ArrayList<>();
        if(existenFiles(fotos)){
            fotosGatitos=this.guardarFotos(fotos, nuevo);
        }
        gato.setFotos(fotosGatitos);
        return this.gatoRepo.save(gato);
    }

    private boolean existenFiles(MultipartFile[] files){
        boolean existe=false;
        //logger.info("files="+files);
        if(files!=null){
            existe=true;
        }
        return existe;
    }

    @Override
    @Transactional
    public Gato modiGato(Gato gato,MultipartFile[] files, Long id) {
        Voluntario volGato=this.voluService.buscarVolByEmailOrException(gato.getVoluntario().getPersona().getEmail());
        gato.setVoluntario(volGato);
        Gato gatodb=this.findGatoById(id);
        //si ya fue adoptado en teoria no lo editaria pero igual
        if(gatodb.getAdoptadoFecha()!=null){
            gato.setAdoptadoFecha(gatodb.getAdoptadoFecha());
        }
        if(gatodb.getFichaVet()!=null){
            gato.setFichaVet(gatodb.getFichaVet());
        }
        if(gatodb.getTransito()!=null){
            gato.setTransito(gatodb.getTransito());
        }
        if(gatodb.getPadrino()!=null){
            gato.setPadrino(gatodb.getPadrino());
        }
        List<Foto> fotosMantener=this.eliminarFotos(gato, id);
        if(existenFiles(files)){
            List<Foto> fotosGatitos = this.guardarFotos(files, gatodb);
            fotosMantener.addAll(fotosGatitos);
        }
        gato.setId(id);
        gato.setFotos(fotosMantener);
        return this.gatoRepo.save(gato);
    }

    private List<Foto> eliminarFotos(Gato gatoMod, Long id){
        Gato gatodb=this.findGatoById(id);
        List<Foto> fotosDb = gatodb.getFotos(); //fotos de gato en db
        if(gatoMod.getFotos().size()< fotosDb.size()){
            List<String> urlsMantener = gatoMod.getFotos().stream()
                    .map(Foto::getFotoUrl)
                    .collect(Collectors.toList());
            List<Foto> fotosAEliminar = fotosDb.stream()
                    .filter(fotoDb -> !urlsMantener.contains(fotoDb.getFotoUrl()))
                    .collect(Collectors.toList());
            for(Foto foto:fotosDb){
                if (!urlsMantener.contains(foto.getFotoUrl())) {
                    foto.setGato(null); // Desvincular la foto del gato
                    fotosAEliminar.add(foto);
                }
            }
            for(Foto foto:fotosAEliminar){
                this.fotoRepo.deleteByFotoUrl(foto.getFotoUrl());
            }
            List<Foto> fotosMantener = fotosDb.stream()
                    .filter(foto -> !fotosAEliminar.contains(foto))
                    .collect(Collectors.toList());
            return fotosMantener;
        }else{
            return fotosDb;
        }

    }

    private List<Foto> guardarFotos(MultipartFile[] files, Gato gato){
        List<Foto> fotosGatitos=new ArrayList<>();
        try {
            List<Map> results=this.cloudService.upload(files);
            for(Map result:results){
                Foto foto=new Foto(0L, (String) result.get("original_filename"),
                        (String) result.get("url"), (String) result.get("public_id"),gato);
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
    public Gato agregarFicha(Ficha ficha,MultipartFile file, Long id) {
        Gato gati=this.findGatoById(id);
        logger.info("ficha="+ficha);
        Ficha nueva=(ficha.getId()!=null)
                ?this.fichaService.editar(ficha,file,ficha.getId())
                :this.fichaService.crear(ficha,file);
        gati.setFichaVet(nueva);
        return this.gatoRepo.save(gati);
    }

    @Override
    //si tenia un transito anterior se va a pisar
    public Gato agregarTransito(Transito transito, Long id) {
        Gato gati=this.findGatoById(id);
        if(gati.getAdoptadoFecha()!=null){
            throw new ExistingException("El gato ya fue adoptado!");
        }
        Transito transitodb=this.tranSer.findByIdOrException(transito.getId());
        gati.setTransito(transitodb);
        Transito tran=this.tranSer.addGato(gati,transitodb);
        if(gati.getPadrino() !=null){
            //agregar al padrino la creacion de notificaciones
        }
        return this.gatoRepo.save(gati);
    }

    @Override
    public Ficha verFichaByGato(Long id) {
        Gato gato=this.verById(id);
        return gato.getFichaVet();
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
