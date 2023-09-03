package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Gato;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.service.interfaces.IMercadoPagoService;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.payment.*;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResponse;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.resources.preference.PreferenceBackUrls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MercadoPagoService implements IMercadoPagoService {
    private Logger logger= LoggerFactory.getLogger(MercadoPagoService.class);

    @Override
    public void crearPago(Gato gato) throws MPException, MPApiException {
        crearPreferencia();
        /*PaymentClient client=new PaymentClient();
        List<PaymentItemRequest> items=new ArrayList<>();

        PaymentItemRequest item= PaymentItemRequest.builder()
                .id("CAT "+gato.getId())
                .title("Apadrinamiento")
                .description("Cuota de "+gato.getNombre())
                .unitPrice(BigDecimal.valueOf(gato.getCuotaMensual()))
                .build();
        items.add(item);

        PaymentCreateRequest createRequest=
                PaymentCreateRequest.builder()
                        .additionalInfo(
                                PaymentAdditionalInfoRequest.builder()
                                        .items(items)
                                        .payer(
                                                PaymentAdditionalInfoPayerRequest.builder()
                                                        .firstName("Name")
                                                        .lastName("LastName")
                                                        .phone(
                                                                PhoneRequest.builder().number("2914585")
                                                                        .build())
                                                        .build())
                                        .shipments(
                                                PaymentShipmentsRequest.builder()
                                                        .receiverAddress(
                                                                PaymentReceiverAddressRequest.builder()
                                                                        .cityName("Cipolletti")
                                                                        .build())
                                                        .build())
                                        .build())
                        .description("Payment for cat")
                        .externalReference("MP001")
                        .installments(1)
                        .order(PaymentOrderRequest.builder()
                                .type("mercadolibre")
                                .id(1L)
                                .build())
                        .payer(PaymentPayerRequest.builder().entityType("individual").type("customer").build())
                        .paymentMethodId("visa")
                        .transactionAmount(BigDecimal.valueOf(gato.getCuotaMensual()))
                        .build();
        try{
            Payment result=client.create(createRequest);
            logger.info("result="+result);
        }
        catch (MPException | MPApiException ex){
            logger.error(ex.getLocalizedMessage(),ex);
        }*/
    }

    private void crearPreferencia() throws MPException, MPApiException {
        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id("1234")
                        .title("Apadrinamiento")
                        .description("Gatito")
                        .pictureUrl("http://picture.com/PS5")
                        .categoryId("padrinos")
                        .quantity(1)
                        .currencyId("BRL")
                        .unitPrice(new BigDecimal("4000"))
                        .build();
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);


        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:9090/generic")
                        .failure("http://localhost:9090/generic")
                        .build())
                .build();
        PreferenceClient client = new PreferenceClient();
        try{
            Preference preference = client.create(preferenceRequest);
            logger.info("preferenceId:"+preference.getId());
            logger.info("preferenceResponse:"+preference.getResponse());
            logger.info("preferenceURL:"+preference.getBackUrls());
        }catch (MPException | MPApiException ex){
            logger.error(ex.getLocalizedMessage(),ex);
        }
    }
}
