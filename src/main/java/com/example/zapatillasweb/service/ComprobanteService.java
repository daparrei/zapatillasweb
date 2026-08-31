package com.example.zapatillasweb.service;

import org.springframework.stereotype.Service;
import com.example.zapatillasweb.repository.ComprobanteRepository;
import com.example.zapatillasweb.entity.Comprobante;
import com.example.zapatillasweb.repository.ClienteRepository;
import java.util.Optional;
import java.util.List;

@Service 
public class ComprobanteService {
    
    private final ComprobanteRepository comprobanteRepository;
    private final ClienteRepository clienteRepository;

    public ComprobanteService(ComprobanteRepository comprobanteRepository, ClienteRepository clienteRepository) {
        this.comprobanteRepository = comprobanteRepository;
        this.clienteRepository = clienteRepository;
    }

    public Optional<Comprobante> obtenerComprobante(Long id) {
        return comprobanteRepository.findById(id);
    }

    public Comprobante crearComprobante(Comprobante comprobante) {
       
        //Validar que tenga cliente
        if (comprobante.getCliente() == null || comprobante.getCliente().getId() == null) {
            throw new IllegalArgumentException("El comprobante debe tener un cliente válido.");
        }

        //Validar que el cliente exista

        Long clienteId = comprobante.getCliente().getId();


        if (!clienteRepository.existsById(clienteId)) {
            throw new IllegalArgumentException("El cliente con ID " + clienteId + " no existe.");
        }
                
       return comprobanteRepository.save(comprobante);
    }

    public boolean eliminarComprobante(Long id) {
        if (comprobanteRepository.existsById(id)) {
            comprobanteRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Comprobante> obtenerComprobantes() {
        return comprobanteRepository.findAll();
    }
}
