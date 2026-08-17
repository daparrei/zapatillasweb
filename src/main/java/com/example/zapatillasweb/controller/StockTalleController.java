package com.example.zapatillasweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.zapatillasweb.service.StockTalleService;
import com.example.zapatillasweb.entity.StockTalle;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/stock-talles")
public class StockTalleController {
    
    private final StockTalleService stockTalleService;

    public StockTalleController(StockTalleService stockTalleService) {
        this.stockTalleService = stockTalleService;
    }

    @GetMapping(
        value = "/{id}", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getStockTalleById(
            @PathVariable Long id) 
    {
        return stockTalleService.obtenerStockTalle(id)
                .map(stockTalle -> ResponseEntity.ok(stockTalle))
                .orElse(ResponseEntity.notFound().build());
    }

    //Buscar todos los talles de un producto
    @GetMapping(
        value = "/producto/{productoId}", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )   
    public ResponseEntity<?> obtenerStockPorProducto(
            @PathVariable Long productoId) 
    {
        List<StockTalle> stockTalles = stockTalleService.obtenerStockPorProducto(productoId);
        if (stockTalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(stockTalles);
        }
    }

    //Buscar un talle específico de un producto
    @GetMapping(
        value = "/producto/{productoId}/talle/{talle}", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> obtenerStockTallePorProductoYTalle(
            @PathVariable Long productoId,
            @PathVariable String talle) 
    {
        Optional<StockTalle> stockTalle = stockTalleService.obtenerStockTallePorProductoYTalle(productoId, talle);
        if (stockTalle.isPresent()) {
            return ResponseEntity.ok(stockTalle.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Crear un nuevo registro de stock talle
    @PostMapping(
        value = "/producto/{productoId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> crearStockTalle(
            @PathVariable Long productoId,
            @RequestBody StockTalle stockTalle) {

        try {
            StockTalle nuevoStockTalle =
                    stockTalleService.crearStockTalle(
                            productoId,
                            stockTalle);

            return ResponseEntity.ok(nuevoStockTalle);

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error al crear el stock talle: " + e.getMessage());
        }
    }
    //Eliminar un registro de stock talle
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarStockTalle(
            @PathVariable Long id) {

        boolean eliminado = stockTalleService.eliminarStockTalle(id);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Actualizar un registro de stock talle
    @PutMapping(
        value = "/producto/{productoId}/talle/{talle}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> actualizarStockTalle(
            @PathVariable Long productoId,
            @PathVariable String talle,
            @RequestBody StockTalle nuevoStock) {

        Optional<StockTalle> stockTalleActualizado =
                stockTalleService.actualizarStock(productoId, talle, nuevoStock.getStock());

        if (stockTalleActualizado.isPresent()) {
            return ResponseEntity.ok(stockTalleActualizado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Sumar Stock
    @PostMapping("/producto/{productoId}/talle/{talle}/sumar-stock")

    public ResponseEntity<?> sumarStock(
            @PathVariable Long productoId,
            @PathVariable String talle,
            @RequestBody StockTalle stockTalle) {

        Optional<StockTalle> stockTalleActualizado =
                stockTalleService.sumarStock(productoId, talle, stockTalle.getStock());

        if (stockTalleActualizado.isPresent()) {
            return ResponseEntity.ok(stockTalleActualizado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

      // Restar Stock
    @PostMapping("/producto/{productoId}/talle/{talle}/restar-stock")

    public ResponseEntity<?> restarStock(
            @PathVariable Long productoId,
            @PathVariable String talle,
            @RequestBody StockTalle stockTalle) {

        Optional<StockTalle> stockTalleActualizado =
                stockTalleService.restarStock(productoId, talle, stockTalle.getStock());

        if (stockTalleActualizado.isPresent()) {
            return ResponseEntity.ok(stockTalleActualizado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
