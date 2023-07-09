package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.dto.mapper.IAspiranteVoluntarioMapper;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.*;
import com.example.gatosspringboot.repository.database.AspiranteRepository;
import com.example.gatosspringboot.service.interfaces.IAspiranteService;
import com.example.gatosspringboot.service.interfaces.IEstadoService;
import com.example.gatosspringboot.service.interfaces.ISocioService;
import com.example.gatosspringboot.service.interfaces.IVoluntarioService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AspiranteService implements IAspiranteService {

    private final AspiranteRepository repo;
    private final IEstadoService estadoService;
    private final ISocioService socioSer;
    private final IAspiranteVoluntarioMapper aspiVoluMap;
    private final IVoluntarioService voluService;
    private Logger logger= LoggerFactory.getLogger(AspiranteService.class);

    public AspiranteService(AspiranteRepository repo,
                            IEstadoService estadoService,
                            ISocioService socioSer,
                            IAspiranteVoluntarioMapper aspiVoluMap,
                            IVoluntarioService voluService) {
        this.repo = repo;
        this.estadoService = estadoService;
        this.socioSer = socioSer;
        this.aspiVoluMap = aspiVoluMap;
        this.voluService = voluService;
    }

    @Override
    public Aspirante altaAspirante(Aspirante aspirante) {
        Estado pendiente=this.estadoService.crearPendiente();
        aspirante.setEstados(new ArrayList<>(Arrays.asList(pendiente)));
        return this.repo.save(aspirante);
    }

    @Override
    @Transactional
    //aca debo traer el socio que lo acepto
    //se crea el voluntario/transito
    public Aspirante aceptarAspirante(Aspirante aspirante, Long id) {
        Aspirante aspi = this.buscarById(id);
        //agregar estado aceptado
        List<Estado> estados = aspi.getEstados();
        Estado aceptado = estadoService.crearAprobado();
        estados.add(aceptado);
        //crearse como voluntario, padrino o transito
        this.crearTipoVoluntariado(aspi);
        //busco el socio q lo acepto por el email asi se manda segun la sesion
        String emailSocio=aspirante.getSocio().getEmail();
        Socio socio=this.socioSer.buscarByEmail(emailSocio);
        aspi.setSocio(socio);
        return this.repo.save(aspi);
    }

    @Override
    //aca debo traer el socio que lo rechazo
    public Aspirante rechazarAspirante(Aspirante aspirante, Long id) {
        return null;
    }

    private Aspirante buscarById(Long id){
        Optional<Aspirante> oAspi=this.repo.findById(id);
        if(oAspi.isEmpty()){
            throw new NonExistingException(
                    String.format("El aspirante con id %d no existe",id));
        }
        return oAspi.get();
    }

    //falta el padrino
    private void crearTipoVoluntariado(Aspirante aspirante){
        List<TipoVoluntariado> voluntariados=aspirante.getTiposVoluntariado();
        if(voluntariados.contains(TipoVoluntariado.VOLUNTARIO)){
            Voluntario volu=this.aspiVoluMap.mapAspiranteToVoluntario(aspirante);
            //logger.info("volu de aspirante service: "+volu);
            this.voluService.altaVolunt(volu);
        }
        if(voluntariados.contains(TipoVoluntariado.TRANSITO)){
            //darlo de alta como transito,
        }
    }

    @Override
    public List<Aspirante> listarTodos() {
        return (List<Aspirante>) this.repo.findAll();
    }

    @Override
    public List<Aspirante> listarByEstado(Estado estado) {
        return null;
    }

    @Override
    public List<Aspirante> listarBySocio(Socio socio) {
        return null;
    }
}
