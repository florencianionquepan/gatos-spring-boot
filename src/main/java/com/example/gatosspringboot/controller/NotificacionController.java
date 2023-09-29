package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.model.Notificacion;
import com.example.gatosspringboot.service.interfaces.INotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final INotificacionService service;

    public NotificacionController(INotificacionService service) {
        this.service = service;
    }

    public Map<String,Object> mensajeBody= new HashMap<>();

    private ResponseEntity<?> successResponse(Object data){
        mensajeBody.put("success",Boolean.TRUE);
        mensajeBody.put("data",data);
        return ResponseEntity.ok(mensajeBody);
    }

    @GetMapping("persona/{email}")
    public ResponseEntity<?> verByPersona(@PathVariable String email){
        List<Notificacion> noti=this.service.verByPersona(email);
        return this.successResponse(noti);
    }
}
