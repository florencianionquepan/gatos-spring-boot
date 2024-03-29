package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;

import java.util.List;

public interface IPadrinoService {
    Padrino buscarByEmailOrException(String email);
    void notificarAdopcion(Padrino padrino, Gato gato);
    void notificarTransitoNuevo(Padrino padrino, Gato gato);
    Padrino removerGato(String email, Gato gato);
    //Padrino revisarCuotasImpagas(Long id);
    List<Cuota> revisarCuotasImpagas(String dni);
}
