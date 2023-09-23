package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Ficha;
import com.example.gatosspringboot.repository.database.FichaRepository;
import com.example.gatosspringboot.service.interfaces.IAmazonService;
import com.example.gatosspringboot.service.interfaces.IFichaService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class FichaService implements IFichaService {

    private final FichaRepository repo;
    private final IAmazonService amazonSer;

    public FichaService(FichaRepository repo,
                        IAmazonService amazonSer) {
        this.repo = repo;
        this.amazonSer = amazonSer;
    }

    @Override
    public Ficha crear(Ficha ficha, MultipartFile file) {
        if(!file.isEmpty()){
            String fileName=this.amazonSer.uploadFile(file);
            ficha.setPdf(fileName);
        }
        return this.repo.save(ficha);
    }

    @Override
    public Ficha editar(Ficha ficha, MultipartFile file, Long idFicha) {
        Optional<Ficha> oFicha=this.repo.findById(idFicha);
        if(oFicha.isEmpty()){
            throw new NonExistingException("La ficha no existe");
        }
        ficha.setId(idFicha);
        return this.crear(ficha,file);
    }

}
