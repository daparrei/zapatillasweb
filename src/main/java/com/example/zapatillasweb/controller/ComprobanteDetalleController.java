package com.example.zapatillasweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.zapatillasweb.entity.ComprobanteDetalle;
import com.example.zapatillasweb.service.ComprobanteDetalleService;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;


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

    @PostMapping (
        consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<?> crearComprobanteDetalle(
        @RequestBody ComprobanteDetalle comprobanteDetalle
    ) {
        try {
            ComprobanteDetalle nuevoComprobanteDetalle = 
                comprobanteDetalleService.crearComprobanteDetalle(comprobanteDetalle);
            return ResponseEntity
                .created(URI.create("/comprobantes-detalle/"+ nuevoComprobanteDetalle.getId()))
                .body(nuevoComprobanteDetalle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    @PutMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> actualizarComprobanteDetalle(
            @PathVariable(name = "id") Long id,
            @RequestBody ComprobanteDetalle comprobanteDetalle
    ) {
        try {
            ComprobanteDetalle comprobanteDetalleActualizado = comprobanteDetalleService.actualizarComprobanteDetalle(id, comprobanteDetalle);
            return ResponseEntity.ok(comprobanteDetalleActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}