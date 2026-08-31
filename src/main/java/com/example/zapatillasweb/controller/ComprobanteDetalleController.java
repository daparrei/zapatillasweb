package com.example.zapatillasweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.zapatillasweb.entity.ComprobanteDetalle;
import com.example.zapatillasweb.service.ComprobanteDetalleService;
import org.springframework.http.MediaType;

import java.util.Optional;

@RestController
@RequestMapping("/comprobantes-detalle")
public class ComprobanteDetalleController {
    
    private final ComprobanteDetalleService comprobanteDetalleService;

    public ComprobanteDetalleController(
            ComprobanteDetalleService comprobanteDetalleService) {
        this.comprobanteDetalleService = comprobanteDetalleService;
    }

   @GetMapping(
    value = "/{id}", 
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ComprobanteDetalle> getComprobanteById(
            @PathVariable (name = "id") Long id) 
    {
        Optional<ComprobanteDetalle> comprobanteDetalle  =
            comprobanteDetalleService.obtenerComprobanteDetalle(id);
        if (comprobanteDetalle.isPresent()) {
            return ResponseEntity.ok(comprobanteDetalle.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }   
}
