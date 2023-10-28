package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.service.interfaces.IMercadoPagoService;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.payment.*;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService implements IMercadoPagoService {
    private Logger logger= LoggerFactory.getLogger(MercadoPagoService.class);

    @Override
    public void crearPago(Cuota cuota) throws MPException, MPApiException {

    }

    @Override
    public String crearPreferencia(Cuota cuota) throws MPException, MPApiException {
        LocalDate fecha=LocalDate.now();
        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id("1234")
                        .title("Apadrinamiento "+cuota.getGato().getNombre())
                        .description("GatoId="+cuota.getGato().getId()+" Mes Cuota="+fecha.getMonth())
                        .pictureUrl("http://picture.com/PS5")
                        .categoryId("Apadrinamientos")
                        .quantity(1)
                        .currencyId("ARS")
                        .unitPrice(new BigDecimal(cuota.getGato().getMontoMensual()))
                        .build();
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);


        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:9090/generic")
                        .failure("http://localhost:9090/generic")
                        .build())
                .additionalInfo(String.valueOf(PaymentAdditionalInfoRequest.builder()
                        .payer(PaymentAdditionalInfoPayerRequest.builder()
                                .firstName(cuota.getPadrino().getPersona().getNombre())
                                .lastName(cuota.getPadrino().getPersona().getApellido())
                                .phone(
                                        PhoneRequest.builder().number(cuota.getPadrino().getPersona().getTel())
                                                .build())
                                .build())
                        .build()))
                        .build();
        PreferenceClient client = new PreferenceClient();
        String response="";
        try{
            Preference preference = client.create(preferenceRequest);
            logger.info("preferenceId:"+preference.getId());
            logger.info("url:"+preference.getSandboxInitPoint());
            response=preference.getSandboxInitPoint();
            //logger.info("urlc:"+preference.getInitPoint());
            //logger.info("preferenceResponse:"+preference.getResponse());
            //logger.info("preferenceURL:"+preference.getBackUrls());
        }catch (MPException | MPApiException ex){
            logger.error(ex.getLocalizedMessage(),ex);
        }
        return response;
    }

    //    State: APROVED
    //    Type: Mastercard
    //    Number:    5031755734530604
    //    CVV: 123
    //    Expire at: 11/25
    //    Holder: APRO GOMEZ
    //    DNI: 31256588
    //    Email: apro_gomez@gmail.com
    //---------------------------------
    //    State: REJECTED
    //    Type: Mastercard
    //    Number:    5031755734530604
    //    CVV: 123
    //    Expire at: 11/25
    //    Holder: EXPI GOMEZ
    //    DNI: 31256588
    //    Email: expi_gomez@gmail.com
}
