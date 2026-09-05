package com.example.zapatillasweb.service;

import org.springframework.stereotype.Service;
import com.example.zapatillasweb.repository.ComprobanteRepository;
import com.example.zapatillasweb.entity.Comprobante;
import com.example.zapatillasweb.entity.ComprobanteDetalle;
import com.example.zapatillasweb.repository.ClienteRepository;
import com.example.zapatillasweb.entity.Cliente;
import com.example.zapatillasweb.api.WorldclockApi;
import java.util.Optional;
import java.util.List;

@Service 
public class ComprobanteService {
    
    private final ComprobanteRepository comprobanteRepository;
    private final ClienteRepository clienteRepository;
    private final StockTalleService stockTalleService;
    private final WorldclockApi worldclockApi;


    public ComprobanteService(ComprobanteRepository comprobanteRepository, ClienteRepository clienteRepository, StockTalleService stockTalleService, WorldclockApi worldclockApi) {
        this.comprobanteRepository = comprobanteRepository;
        this.clienteRepository = clienteRepository;
        this.stockTalleService = stockTalleService;
        this.worldclockApi = worldclockApi;
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
            comprobante.setCreatedAt(worldclockApi.getCurrentTime());
        }

        //5) Si el comprobante no tiene total, asignar 0
        if (comprobante.getTotal() == null) {
            comprobante.setTotal(java.math.BigDecimal.ZERO);
        }
        


       return comprobanteRepository.save(comprobante);
    }

    public Comprobante actualizarComprobante(Long id, Comprobante comprobanteActualizado) {
        Optional<Comprobante> comprobanteOptional = comprobanteRepository.findById(id);
        if (comprobanteOptional.isPresent()) {
            
            Comprobante comprobanteExistente = comprobanteOptional.get();
            // Actualizar los campos del comprobante existente con los valores del comprobante actualizado
            
            //Validar el nuevo status del comprobante
            validarNuevoStatusComprobante(comprobanteExistente.getStatus(), comprobanteActualizado.getStatus());    
            
            //Si el nuevo status es cancelado hay que liberar el stock de los productos del comprobante
            if (comprobanteActualizado.getStatus().equals("CANCELADO")) {
                
                // Logica para liberar el stock de los productos del comprobante 
                List<ComprobanteDetalle> detalles = comprobanteExistente.getDetalles();
                for (ComprobanteDetalle detalle : detalles) {
                    Long productoId = detalle.getProducto().getId();
                    String talle = detalle.getTalle();
                    Integer cantidad = detalle.getCantidad();

                    // Llamar al servicio de stock para sumar el stock del producto y talle
                    stockTalleService.sumarStock(productoId, talle, cantidad);
                }
            }
            
            comprobanteExistente.setStatus(comprobanteActualizado.getStatus());
            // Actualizar otros campos según sea necesario
            return comprobanteRepository.save(comprobanteExistente);
        } else {
            throw new IllegalArgumentException("El comprobante con ID " + id + " no existe.");
        }
    }
    //La función validarStatusComprobante valida que el estado del comprobante sea uno de los valores permitidos: "NUEVO", "PENDIENTE", "PAGADO" o "CANCELADO". Si el estado no es válido, lanza una excepción IllegalArgumentException con un mensaje descriptivo.
    public void validarStatusComprobante(String status) {
        if (status == null || (!status.equals("NUEVO") && !status.equals("PENDIENTE") && !status.equals("PAGADO") && !status.equals("CANCELADO"))) {
            throw new IllegalArgumentException("El estado del comprobante debe ser 'NUEVO', 'PENDIENTE', 'PAGADO' o 'CANCELADO'.");
        }
    }

    //VALIDA CON UN CASE EL SIGUIETE STATUS DEL COMPROBANTE, SI EL STATUS ES NUEVO SOLO PUEDE CAMBIAR A PENDIENTE O CANCELADO, SI EL STATUS ES PENDIENTE SOLO PUEDE CAMBIAR A PAGADO O CANCELADO, SI EL STATUS ES PAGADO NO PUEDE CAMBIAR A NINGUN OTRO STATUS, SI EL STATUS ES CANCELADO NO PUEDE CAMBIAR A NINGUN OTRO STATUS
    public void validarNuevoStatusComprobante(String status, String nuevoStatus) {
        
        validarStatusComprobante(nuevoStatus);

        switch (status) {
            case "NUEVO":
                if (!nuevoStatus.equals("PENDIENTE") && !nuevoStatus.equals("CANCELADO")) {
                    throw new IllegalArgumentException("El estado del comprobante 'NUEVO' solo puede cambiar a 'PENDIENTE' o 'CANCELADO'.");
                }
                break;
            case "PENDIENTE":
                if (!nuevoStatus.equals("PAGADO") && !nuevoStatus.equals("CANCELADO")) {
                    throw new IllegalArgumentException("El estado del comprobante 'PENDIENTE' solo puede cambiar a 'PAGADO' o 'CANCELADO'.");
                }
                break;
            case "PAGADO":
                throw new IllegalArgumentException("El estado del comprobante 'PAGADO' no puede cambiar a ningún otro estado.");
            case "CANCELADO":
                throw new IllegalArgumentException("El estado del comprobante 'CANCELADO' no puede cambiar a ningún otro estado.");
            default:
                throw new IllegalArgumentException("Estado de comprobante desconocido: " + status);
        }
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
