package com.example.zapatillasweb.controller;

import com.example.zapatillasweb.entity.Producto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.zapatillasweb.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.Optional;



@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping(
    value = "/{id}", 
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getProductById(
            @PathVariable (name = "id") Long id) 
    {
        Optional<Producto> producto = productoService.obtenerProducto(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping (
        consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<?> guardarProducto(
        @RequestBody Producto producto
    ) {
        try {
            Producto nuevoProducto = 
                productoService.crearProducto(producto);
            return ResponseEntity
                .created(URI.create("/producto/"+ nuevoProducto.getId()))
                .body(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el producto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        // Lógica para eliminar un producto
        boolean eliminado = productoService.eliminarProducto(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
