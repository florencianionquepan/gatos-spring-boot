package com.example.gatosspringboot.service.interfaces;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public interface IMercadoPagoService {
    void crearPago(Gato gato) throws MPException, MPApiException;
}
