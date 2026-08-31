package com.example.zapatillasweb.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.zapatillasweb.entity.Comprobante;
import com.example.zapatillasweb.service.ComprobanteService;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.Optional;
import java.util.List;  

@RestController
@RequestMapping("/comprobantes")
public class ComprobanteController {

    private final ComprobanteService comprobanteService;

    public ComprobanteController(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
      }

    @GetMapping(
    value = "/{id}", 
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getComprobanteById(
            @PathVariable (name = "id") Long id) 
    {
        Optional<Comprobante> comprobante = comprobanteService.obtenerComprobante(id);
        if (comprobante.isPresent()) {
            return ResponseEntity.ok(comprobante.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping (
        consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<?> guardarComprobante(
        @RequestBody Comprobante comprobante
    ) {
        try {
            Comprobante nuevoComprobante = 
                comprobanteService.crearComprobante(comprobante);
            return ResponseEntity
                .created(URI.create("/comprobantes/"+ nuevoComprobante.getId()))
                .body(nuevoComprobante);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    @GetMapping
        public ResponseEntity<List<Comprobante>> getComprobantes() {
        return ResponseEntity.ok(comprobanteService.obtenerComprobantes());
    }   


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComprobante(@PathVariable Long id) {
        // Lógica para eliminar un cliente
        boolean eliminado = comprobanteService.eliminarComprobante(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
