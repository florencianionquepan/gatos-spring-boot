package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.EstadoPago;
import com.example.gatosspringboot.repository.database.CuotaRepository;
import com.example.gatosspringboot.service.interfaces.IMercadoPagoService;
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
    private final CuotaRepository cuotaRepo;
    private Logger logger= LoggerFactory.getLogger(MercadoPagoService.class);

    public MercadoPagoService(CuotaRepository cuotaRepo) {
        this.cuotaRepo = cuotaRepo;
    }

    @Override
    public String crearPreferencia(Cuota cuota) throws MPException, MPApiException {
        LocalDate fecha=LocalDate.now();
        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id(String.valueOf(cuota.getId()))
                        .title("Apadrinamiento "+cuota.getGato().getNombre())
                        .description("GatoId="+cuota.getGato().getId()+" Mes Cuota="+fecha.getMonth())
                        .pictureUrl("http://picture.com/PS5")
                        .categoryId("Apadrinamientos")
                        .quantity(1)
                        .currencyId("BRL")
                        .unitPrice(new BigDecimal(cuota.getGato().getMontoMensual()))
                        .build();
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:9090/cuotas/generic")
                        .failure("http://localhost:9090/cuotas/generic")
                        .build())
                .build();
        PreferenceClient client = new PreferenceClient();
        String response="";
        try{
            Preference preference = client.create(preferenceRequest);
            logger.info("preferenceId:"+preference.getId());
            logger.info("url:"+preference.getSandboxInitPoint());
            cuota.setPreferencia_id(preference.getId());
            cuota.setEstadoPago(EstadoPago.DESCONOCIDO);
            this.cuotaRepo.save(cuota);
            response=preference.getSandboxInitPoint();
        }catch (MPException | MPApiException ex){
            logger.error(ex.getLocalizedMessage(),ex);
        }
        return response;
    }

}
