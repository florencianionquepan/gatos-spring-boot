package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Cuota;

import java.util.List;

public interface ICuotaService {

    List<Cuota> listarByPadrino(String email);
    List<Cuota> actualizarCuotas();
    List<Cuota> listarByEstado(String estado);
    List<Cuota> listarAll();
    String creacionPreferenciaPrimeraCuota(Cuota cuota);
    Cuota creacionCuota(Cuota cuota);
    String creacionPreferencia(Long idCuota);
    Cuota pagoCuotaAprobado(String preferenciaId);
    Cuota pagoCuotaRechazado(String preferenciaId);
}
