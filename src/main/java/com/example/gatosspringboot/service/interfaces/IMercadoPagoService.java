package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Cuota;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public interface IMercadoPagoService {
    void crearPago(Cuota cuota) throws MPException, MPApiException;

    String crearPreferencia(Cuota cuota) throws MPException, MPApiException;
}
