package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.config.MPConfig;
import com.example.gatosspringboot.dto.CuotaDTO;
import com.example.gatosspringboot.dto.mapper.ICuotaMapper;
import com.example.gatosspringboot.model.Cuota;
import com.example.gatosspringboot.service.imple.MercadoPagoService;
import com.example.gatosspringboot.service.interfaces.ICuotaService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.exceptions.MPException;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuotas")
public class CuotaController {

    private final ICuotaService service;
    private final ICuotaMapper mapper;
    @Autowired
    private MPConfig mpconfig;

    public Map<String,Object> mensajeBody= new HashMap<>();
    private Logger logger= LoggerFactory.getLogger(MercadoPagoService.class);

    public CuotaController(ICuotaService service,
                           ICuotaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    @GetMapping("/padrino/{email}")
    public ResponseEntity<?> verByPadrino(@PathVariable String email){
        List<Cuota> cuotas=this.service.listarByPadrino(email);
        return this.successResponse(this.mapper.mapToListDto(cuotas));
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<?> crearCuotaPrimeraVez(@RequestBody CuotaDTO cuota){
        MercadoPagoConfig.setAccessToken(mpconfig.getAccessToken());
        String response=this.service.creacionPreferenciaPrimeraCuota(this.mapper.mapToEntity(cuota));
        return this.successResponse(response);
    }

    @GetMapping("/preferencia/{id}")
    public ResponseEntity<?> realizarPagoRechazadoDesconocido(@PathVariable String id){
        MercadoPagoConfig.setAccessToken(mpconfig.getAccessToken());
        String response="https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id="+id;
        return this.successResponse(response);
    }

    @GetMapping("/pendiente/{idCuota}")
    public ResponseEntity<?> realizarPagoPendiente(@PathVariable Long idCuota){
        MercadoPagoConfig.setAccessToken(mpconfig.getAccessToken());
        String response=this.service.creacionPreferencia(idCuota);
        return this.successResponse(response);
    }

    @GetMapping("/generic")
    public RedirectView generic(
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
        if(collectionStatus.equals("approved")){
            Cuota paga=this.service.pagoCuotaAprobado(preferenceId);
            return new RedirectView("http://localhost:4200/backoffice/cuotas/success");
        }else if(collectionStatus.equals("rejected")){
            Cuota rechazada=this.service.pagoCuotaRechazado(preferenceId);
        }
        return new RedirectView("http://localhost:4200/backoffice/cuotas/failure");
    }

    @GetMapping("/actualizacion")
    @PreAuthorize("hasRole('ROLE_SOCIO')")
    public ResponseEntity<?> actualizarCuotas(){
        List<Cuota> cuotas=this.service.actualizarCuotas();
        return this.successResponse(this.mapper.mapToListDto(cuotas));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ROLE_SOCIO')")
    public ResponseEntity<?> listarByEstado(@PathVariable String estado){
        List<Cuota> cuotas=this.service.listarByEstado(estado);
        return this.successResponse(this.mapper.mapToListDto(cuotas));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SOCIO')")
    public ResponseEntity<?> listarAll(){
        List<Cuota> cuotas=this.service.listarAll();
        return this.successResponse(this.mapper.mapToListDto(cuotas));
    }

}
