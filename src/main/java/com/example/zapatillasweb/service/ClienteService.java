package com.example.zapatillasweb.service;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.zapatillasweb.entity.Cliente;
import com.example.zapatillasweb.repository.ClienteRepository;


@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Optional<Cliente> obtenerCliente(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public boolean eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }
    
}
