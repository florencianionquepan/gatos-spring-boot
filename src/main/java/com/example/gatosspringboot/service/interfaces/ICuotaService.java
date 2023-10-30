package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Cuota;

public interface ICuotaService {

    String creacionPreferencia(Cuota cuota);
    Cuota creacionCuota(Cuota cuota);
    Cuota pagoCuotaAprobado(String preferenciaId);
    Cuota pagoCuotaRechazado(String preferenciaId);
}
