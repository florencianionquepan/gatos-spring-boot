package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.CuotaRepository;
import com.example.gatosspringboot.repository.database.GatoRepository;
import com.example.gatosspringboot.repository.database.PadrinoRepository;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.service.interfaces.ICuotaService;
import com.example.gatosspringboot.service.interfaces.IMercadoPagoService;
import com.example.gatosspringboot.service.interfaces.INotificacionService;
import com.example.gatosspringboot.service.interfaces.IPadrinoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CuotaService implements ICuotaService {

    private final CuotaRepository repo;
    private final GatoRepository gatoRepo;
    private final PersonaRepository persoRepo;
    private final PadrinoRepository padriRepo;
    private final IPadrinoService padriService;
    private final IMercadoPagoService MPservice;
    private final INotificacionService notiser;
    private Logger logger= LoggerFactory.getLogger(CuotaService.class);

    public CuotaService(CuotaRepository repo,
                        GatoRepository gatoRepo,
                        PersonaRepository persoRepo,
                        PadrinoRepository padriRepo,
                        IPadrinoService padriService,
                        IMercadoPagoService mPservice,
                        INotificacionService notiser) {
        this.repo = repo;
        this.gatoRepo = gatoRepo;
        this.persoRepo = persoRepo;
        this.padriRepo = padriRepo;
        this.padriService = padriService;
        MPservice = mPservice;
        this.notiser = notiser;
    }

    @Override
    public List<Cuota> listarByPadrino(String email) {
        Optional<Padrino> oPadri=this.padriRepo.buscarByEmail(email);
        if(oPadri.isEmpty()){
            throw new NonExistingException(
                    String.format("El padrino con email %s no existe",email));
        }
        List<Cuota> cuotas=this.repo.listarByPadrino(email);
        return cuotas;
    }

    @Override
    public List<Cuota> actualizarCuotas() {
        List<Padrino> padrinos= (List<Padrino>) this.padriRepo.findAll();
        logger.info("Comenzando la actualizacion de cuotas por padrino...");
        List<Cuota> cuotasCreadas=new ArrayList<>();
        for(Padrino padri:padrinos){
            //chequear  estado aprobado (fecha de esas cuotas) y ver si hay que crear nuevas
            logger.info("Se revisan las cuotas del padrino: "+padri.getPersona().getNombre());
            List<Cuota> cuotasCreadasByPadrino=this.actualizarCuotasByPadri(padri);
            cuotasCreadas.addAll(cuotasCreadasByPadrino);
        }
        return cuotasCreadas;
    }

    //cuando se adopta un gato se elimina su padrino para no seguir actualizando su cuota
    private List<Cuota> actualizarCuotasByPadri(Padrino padri) {
        List<Gato> gatosPadrino= padri.getListaGatos();
        List<Cuota> cuotas=this.repo.listarByPadrino(padri.getPersona().getEmail());
        LocalDate fechaActual = LocalDate.now();
        List<Cuota> cuotasFiltradas1 = cuotas.stream()
                //revisar si es parte del listado de gatos de padrino
                .filter(cuota->gatosPadrino.stream().anyMatch(gato -> gato.getId() ==cuota.getGato().getId()))
                .collect(Collectors.toList());
        //si hay de mismos gatos, solo devolveme la ultima!;
        List<Cuota> cuotasFiltradas2=new ArrayList<>();
        Map<Long,Cuota> mapCuotas=new HashMap<>();
        for(Cuota cuota:cuotasFiltradas1){
            if(!mapCuotas.containsKey(cuota.getGato().getId())
                    || mapCuotas.containsKey(cuota.getGato().getId()) && cuota.getFechaCreacion().isAfter(mapCuotas.get(cuota.getGato().getId()).getFechaCreacion())){
                    mapCuotas.put(cuota.getGato().getId(),cuota);
            }
        }
        logger.info("Ultimas cuotas por gato:");
        cuotasFiltradas1.forEach(cuota -> {
            logger.info("Del gato: "+cuota.getGato().getNombre());
            logger.info("Fecha de creacion: "+cuota.getFechaCreacion());
        });
        for(Map.Entry<Long,Cuota> entry:mapCuotas.entrySet()){
            cuotasFiltradas2.add(entry.getValue());
        }
        List<Cuota> cuotasCreadas=new ArrayList<>();
        //una vez que obtuve la ultima cuota del gato, ahi chequeo si es de otro mes distinto...
        List<Cuota> cuotasFiltradasFinales = cuotasFiltradas2.stream()
                .filter(cuota -> cuota.getFechaCreacion().getMonth() != fechaActual.getMonth())
                .collect(Collectors.toList());
        if(!cuotasFiltradasFinales.isEmpty()){
            cuotasFiltradasFinales.forEach(cuota->{
                Cuota nueva=new Cuota();
                Gato gatodb=this.findGatoByIdOrException(cuota.getGato());
                nueva.setGato(gatodb);
                nueva.setPadrino(padri);
                LocalDate fecha=LocalDate.now();
                nueva.setFechaCreacion(fecha);
                nueva.setEstadoPago(EstadoPago.PENDIENTE);
                //actualizo monto de cuota al del gato
                nueva.setMontoMensual(gatodb.getMontoMensual());
                logger.info("Nueva cuota para gato: "+nueva.getGato().getNombre());
                this.notiser.actualizacionCuota(padri,gatodb);
                Cuota nuevaGuardada=this.repo.save(nueva);
                cuotasCreadas.add(nuevaGuardada);
            });
        }
        return cuotasCreadas;
    }

    @Override
    public List<Cuota> listarByEstado(String estado) {
        EstadoPago estadoP;
        try {
            estadoP = EstadoPago.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NonExistingException("Ese no es un estado valido");
        }
        return this.repo.findAllByEstadoPago(estadoP);
    }

    @Override
    public List<Cuota> listarAll() {
        return (List<Cuota>) this.repo.findAll();
    }

    @Override
    public String creacionPreferenciaPrimeraCuota(Cuota cuota) {
        Cuota nueva=this.creacionCuota(cuota);
        String response;
        try{
            response=this.MPservice.crearPreferencia(nueva);
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    //este metodo se va a ejecutar cuando pasen 30 dias de cuota aprobada tambien? no al final no.
    public Cuota creacionCuota(Cuota cuota) {
        Gato gatodb=this.findGatoByIdOrException(cuota.getGato());
        cuota.setGato(gatodb);
        Optional<Padrino> oPadri=this.padriRepo.buscarByEmail(cuota.getPadrino().getPersona().getEmail());
        if(oPadri.isEmpty()){
            Optional<Persona> oPerso=this.persoRepo.findByEmail(cuota.getPadrino().getPersona().getEmail());
            if(oPerso.isEmpty()){
                throw new NonExistingException("Debe registrarse para esta accion");
            }else {
                Persona perso = oPerso.get();
                Padrino nuevo = new Padrino(0L, perso, new ArrayList<>(), new ArrayList<>());
                Padrino padrino = this.padriRepo.save(nuevo);
                cuota.setPadrino(padrino);
                //le seteamos al gato el padrino nuevo:
                gatodb.setPadrino(padrino);
                this.gatoRepo.save(gatodb);
            }
        }else{
            //si el padrino ya es padrino
            Padrino padrino=oPadri.get();
            cuota.setPadrino(padrino);
            //si el padrino no es de este gato
            if(!padrino.getListaGatos().contains(gatodb)){
                gatodb.setPadrino(padrino);
                this.gatoRepo.save(gatodb);
            }
        }
        LocalDate fecha=LocalDate.now();
        cuota.setFechaCreacion(fecha);
        cuota.setEstadoPago(EstadoPago.PENDIENTE);
        return this.repo.save(cuota);
    }

    @Override
    public String creacionPreferencia(Long idCuota) {
        Optional<Cuota> oCuota=this.repo.findById(idCuota);
        if(oCuota.isEmpty()){
            throw new NonExistingException(
                    String.format("La cuota con id %d no existe",idCuota)
            );
        }
        String response="";
        try{
            response=this.MPservice.crearPreferencia(oCuota.get());
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    //este es el gato que viene en cuota
    private Gato findGatoByIdOrException(Gato objeto){
        Optional<Gato> oGato=this.gatoRepo.findById(objeto.getId());
        if(oGato.isEmpty()){
            throw new PersonNotFound(
                    String.format("El gato con id %d no existe",objeto.getId())
            );
        }
        return oGato.get();
    }

    @Override
    @Transactional
    public Cuota pagoCuotaAprobado(String preferenciaId) {
        Cuota cuota=this.repo.findByPreferencia_id(preferenciaId);
        LocalDate actual=LocalDate.now();
        cuota.setFechaPago(actual);
        cuota.setEstadoPago(EstadoPago.APROBADO);
        Cuota modi=this.repo.save(cuota);
        return modi;
    }

    @Override
    public Cuota pagoCuotaRechazado(String preferenciaId) {
        Cuota cuota=this.repo.findByPreferencia_id(preferenciaId);
        cuota.setEstadoPago(EstadoPago.RECHAZADO);
        Cuota modi=this.repo.save(cuota);
        return modi;
    }
}
