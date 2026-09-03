package com.example.zapatillasweb.service;

import org.springframework.stereotype.Service;

import com.example.zapatillasweb.repository.ComprobanteDetalleRepository;
import com.example.zapatillasweb.repository.ComprobanteRepository;
import com.example.zapatillasweb.repository.ProductoRepository;
import com.example.zapatillasweb.repository.StockTalleRepository;
import com.example.zapatillasweb.entity.Comprobante;
import com.example.zapatillasweb.entity.ComprobanteDetalle;
import com.example.zapatillasweb.entity.Producto;
import com.example.zapatillasweb.entity.StockTalle;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ComprobanteDetalleService {
    
    private final ComprobanteRepository comprobanteRepository;
    private final ProductoRepository productoRepository;
    private final StockTalleRepository stockTalleRepository;
    private final ComprobanteDetalleRepository comprobanteDetalleRespository;

    
    public ComprobanteDetalleService (ComprobanteDetalleRepository comprobanteDetalleRespository, ComprobanteRepository comprobanteRepository, ProductoRepository productoRepository,StockTalleRepository stockTalleRepository) {
        this.comprobanteDetalleRespository =comprobanteDetalleRespository;
        this.comprobanteRepository = comprobanteRepository;
        this.productoRepository = productoRepository;
        this.stockTalleRepository = stockTalleRepository;
    }

    public Optional<ComprobanteDetalle> obtenerComprobanteDetalle(Long id) {
        return comprobanteDetalleRespository.findById(id);
    }

   public ComprobanteDetalle crearComprobanteDetalle(ComprobanteDetalle detalle) {

    // 1. Validar comprobante
    if (detalle.getComprobante() == null ||
            detalle.getComprobante().getId() == null) {

        throw new IllegalArgumentException(
                "El detalle debe tener un comprobante válido.");
    }

    Long comprobanteId = detalle.getComprobante().getId();

    Optional<Comprobante> comprobanteOptional =
            comprobanteRepository.findById(comprobanteId);

    if (comprobanteOptional.isEmpty()) {
        throw new IllegalArgumentException(
                "El comprobante especificado no existe.");
    }

    //Si el comprobante esta en estado CANCELADO, PAGADO o ENTREGDO, no se puede agregar un detalle. Solo si es PENDIENTE O NUEVO
    if (comprobanteOptional.get().getStatus() == null || !comprobanteOptional.get().getStatus().equals("PENDIENTE") && !comprobanteOptional.get().getStatus().equals("NUEVO")) {
        throw new IllegalArgumentException(
                "El comprobante especificado no se puede modificar porque su estado es: " + comprobanteOptional.get().getStatus());
    }


    // Usamos el comprobante real de la BD
    Comprobante comprobante = comprobanteOptional.get();
    detalle.setComprobante(comprobante);


    // 2. Validar producto
    if (detalle.getProducto() == null ||
            detalle.getProducto().getId() == null) {

        throw new IllegalArgumentException(
                "El detalle debe tener un producto válido.");
    }

    Long productoId = detalle.getProducto().getId();

    Optional<Producto> productoOptional =
            productoRepository.findById(productoId);

    if (productoOptional.isEmpty()) {
        throw new IllegalArgumentException(
                "El producto especificado no existe.");
    }

    Producto producto = productoOptional.get();
    detalle.setProducto(producto);


    // 3. Validar talle
    if (detalle.getTalle() == null ||
            detalle.getTalle().isBlank()) {

        throw new IllegalArgumentException(
                "El talle es obligatorio.");
    }

    String talle = detalle.getTalle();


    // 4. Validar cantidad
    if (detalle.getCantidad() == null ||
            detalle.getCantidad() <= 0) {

        throw new IllegalArgumentException(
                "La cantidad debe ser mayor a cero.");
    }


    // 5. Buscar stock del producto + talle
    Optional<StockTalle> stockOptional =
            stockTalleRepository.findByProductoIdAndTalle(
                    productoId,
                    talle
            );

    if (stockOptional.isEmpty()) {
        throw new IllegalArgumentException(
                "No existe stock para el producto y talle indicado.");
    }

    StockTalle stockTalle = stockOptional.get();


    // 6. Validar stock suficiente
    if (stockTalle.getStock() < detalle.getCantidad()) {

        throw new IllegalArgumentException(
                "No hay stock suficiente. Stock disponible: "
                        + stockTalle.getStock());
    }


    // 7. Tomar precio actual del producto
    detalle.setPrecioCompra(producto.getPrecio());


    // 8. Descontar y guardar stock
    stockTalle.setStock(
            stockTalle.getStock() - detalle.getCantidad()
    );

    stockTalleRepository.save(stockTalle);


    // 9. Guardar detalle
    ComprobanteDetalle detalleGuardado =
            comprobanteDetalleRespository.save(detalle);


    // 10. Calcular importe del detalle
    BigDecimal importeDetalle =
            detalle.getPrecioCompra()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));


    // 11. Actualizar total del comprobante
    comprobante.setTotal(
            comprobante.getTotal().add(importeDetalle)
    );

    comprobanteRepository.save(comprobante);


        return detalleGuardado;
    }

}
