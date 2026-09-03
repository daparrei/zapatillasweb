package com.example.zapatillasweb.service;

import org.springframework.stereotype.Service;
import com.example.zapatillasweb.repository.ComprobanteRepository;
import com.example.zapatillasweb.entity.Comprobante;
import com.example.zapatillasweb.repository.ClienteRepository;
import com.example.zapatillasweb.entity.Cliente;
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
       
        //1)Validar que tenga cliente
        if (comprobante.getCliente() == null || comprobante.getCliente().getId() == null) {
            throw new IllegalArgumentException("El comprobante debe tener un cliente válido.");
        }

        //2) Validar que el cliente exista

        Long clienteId = comprobante.getCliente().getId();

        if (!clienteRepository.existsById(clienteId)) {
            throw new IllegalArgumentException("El cliente con ID " + clienteId + " no existe.");
        }

        Optional<Cliente> clienteOptional =
            clienteRepository.findById(clienteId);

        comprobante.setCliente(clienteOptional.get());
                        
        //3) Si el comprobante no tiene status, asignar "NUEVO"
        if (comprobante.getStatus() == null) {
            comprobante.setStatus("NUEVO");
        }

        //4) Si el comprobante no tiene fecha de creación, asignar la fecha actual
        if (comprobante.getCreatedAt() == null) {
            comprobante.setCreatedAt(java.time.LocalDateTime.now());
        }

        //5) Si el comprobante no tiene total, asignar 0
        if (comprobante.getTotal() == null) {
            comprobante.setTotal(java.math.BigDecimal.ZERO);
        }
        


       return comprobanteRepository.save(comprobante);
    }

    public boolean eliminarComprobante(Long id) {

        //Validar si el comprobante existe y si su estado es PENDIENTE o NUEVO, solo en esos casos se puede eliminar
          
        if (comprobanteRepository.existsById(id)) {

             Optional<Comprobante> comprobanteOptional = comprobanteRepository.findById(id);
            if (comprobanteOptional.isPresent()) {
                Comprobante comprobante = comprobanteOptional.get();
                String status = comprobante.getStatus();
                if (status != null && (status.equals("PENDIENTE") || status.equals("NUEVO"))) {
                    comprobanteRepository.deleteById(id);
                    return true;
                } else {
                    throw new IllegalArgumentException(
                            "El comprobante no se puede eliminar porque su estado es: " + status);
                }
            } else {
                  throw new IllegalArgumentException(
                            "El comprobante no existe.");
         
            }
        } else {
             throw new IllegalArgumentException(
                            "El comprobante no existe.");
        }
    }

    public List<Comprobante> obtenerComprobantes() {
        return comprobanteRepository.findAll();
    }
}
