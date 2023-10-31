package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Cuota;

import java.util.List;

public interface ICuotaService {

    List<Cuota> listarByPadrino(String email);
    String creacionPreferencia(Cuota cuota);
    Cuota creacionCuota(Cuota cuota);
    Cuota pagoCuotaAprobado(String preferenciaId);
    Cuota pagoCuotaRechazado(String preferenciaId);
}
