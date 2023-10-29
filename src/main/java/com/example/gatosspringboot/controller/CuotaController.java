package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.dto.CuotaDTO;
import com.example.gatosspringboot.dto.mapper.ICuotaMapper;
import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.model.Padrino;
import com.example.gatosspringboot.service.imple.MercadoPagoService;
import com.example.gatosspringboot.service.interfaces.ICuotaService;
import com.mercadopago.exceptions.MPException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cuotas")
public class CuotaController {

    private final ICuotaService service;
    private final ICuotaMapper mapper;

    public Map<String,Object> mensajeBody= new HashMap<>();
    private Logger logger= LoggerFactory.getLogger(MercadoPagoService.class);

    public CuotaController(ICuotaService service, ICuotaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    private ResponseEntity<?> notSuccessResponse(String mensaje,int id){
        mensajeBody.put("success",Boolean.FALSE);
        mensajeBody.put("data", String.format(mensaje,id));
        return ResponseEntity
                .badRequest()
                .body(mensajeBody);
    }

    @GetMapping
    public ResponseEntity<?> verByPadrino(Padrino padrino){
        return null;
    }

    @PostMapping("/preferencia")
    public ResponseEntity<?> crearPreferencia(@RequestBody CuotaDTO cuota){
        String response=this.service.creacionPreferencia(this.mapper.mapToEntity(cuota));
        return this.successResponse(response);
    }

    @PostMapping
    public ResponseEntity<?> pagoCuota(@RequestBody CuotaDTO dto){
            Cuota cuotaPaga=this.service.nuevaCuota(this.mapper.mapToEntity(dto));
            return this.successResponse(cuotaPaga);
    }

    @GetMapping("/generic")
    public RedirectView success(
            HttpServletRequest request,
            @RequestParam("collection_id") String collectionId,
            @RequestParam("collection_status") String collectionStatus,
            @RequestParam("external_reference") String externalReference,
            @RequestParam("payment_type") String paymentType,
            @RequestParam("merchant_order_id") String merchantOrderId,
            @RequestParam("preference_id") String preferenceId,
            @RequestParam("site_id") String siteId,
            @RequestParam("processing_mode") String processingMode,
            @RequestParam("merchant_account_id") String merchantAccountId,
            RedirectAttributes attributes)
            throws MPException {
        attributes.addFlashAttribute("genericResponse", true);
        attributes.addFlashAttribute("collection_id", collectionId);
        attributes.addFlashAttribute("collection_status", collectionStatus);
        attributes.addFlashAttribute("external_reference", externalReference);
        attributes.addFlashAttribute("payment_type", paymentType);
        attributes.addFlashAttribute("merchant_order_id", merchantOrderId);
        attributes.addFlashAttribute("preference_id",preferenceId);
        attributes.addFlashAttribute("site_id",siteId);
        attributes.addFlashAttribute("processing_mode",processingMode);
        attributes.addFlashAttribute("merchant_account_id",merchantAccountId);
        logger.info("attributes="+attributes.getAttribute(collectionStatus));
        if(attributes.getAttribute(collectionStatus).equals("approved")){
            //Cuota nueva=this.service.nuevaCuota()
        }
        return new RedirectView("http://localhost:4200/");
    }

//    @GetMapping("/preferencia/{id}")
//    public RedirectView obtenerPreferencia(@PathVariable String id){
//        return new RedirectView("https://sandbox.mercadopago.com.ar/checkout/preferences/"+id);
//    }

}
