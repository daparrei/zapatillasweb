package com.example.zapatillasweb.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.zapatillasweb.service.ClienteService;
import com.example.zapatillasweb.entity.Cliente;

import java.util.Optional;
import java.net.URI;

@Controller
@RequestMapping ("/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping(
    value = "/{id}", 
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getClienteById(
            @PathVariable (name = "id") Long id) 
    {
        Optional<Cliente> cliente = clienteService.obtenerCliente(id);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

     @PostMapping (
        consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<?> guardarCliente(
        @RequestBody Cliente cliente
    ) {
        try {
            Cliente nuevoCliente = 
                clienteService.crearCliente(cliente);
            return ResponseEntity
                .created(URI.create("/clientes/"+ nuevoCliente.getId()))
                .body(nuevoCliente);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        // Lógica para eliminar un cliente
        boolean eliminado = clienteService.eliminarCliente(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
