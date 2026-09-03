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

    // Actualizar un detalle existente
        public ComprobanteDetalle actualizarComprobanteDetalle(Long id, ComprobanteDetalle detalle) {

                // 1. Validar ID del detalle
                if (id == null) {
                        throw new IllegalArgumentException(
                                "El detalle debe tener un ID válido.");
                }

                // 2. Buscar el detalle existente
                Optional<ComprobanteDetalle> detalleActualOptional =
                        comprobanteDetalleRespository.findById(id);

                if (detalleActualOptional.isEmpty()) {
                        throw new IllegalArgumentException(
                                "El detalle especificado no existe.");
                }

                ComprobanteDetalle detalleActual =
                        detalleActualOptional.get();


                // 3. Validar comprobante
                if (detalle.getComprobante() == null ||
                        detalle.getComprobante().getId() == null) {

                        throw new IllegalArgumentException(
                                "El detalle debe tener un comprobante válido.");
                }

                Long comprobanteId =
                        detalle.getComprobante().getId();


                // 4. Buscar comprobante real en BD
                Optional<Comprobante> comprobanteOptional =
                        comprobanteRepository.findById(comprobanteId);

                if (comprobanteOptional.isEmpty()) {
                        throw new IllegalArgumentException(
                                "El comprobante especificado no existe.");
                }

                Comprobante comprobante =
                        comprobanteOptional.get();


                // 5. Validar estado del comprobante
                if (comprobante.getStatus() == null ||
                        (!comprobante.getStatus().equals("PENDIENTE") &&
                        !comprobante.getStatus().equals("NUEVO"))) {

                        throw new IllegalArgumentException(
                                "El comprobante especificado no se puede modificar porque su estado es: "
                                        + comprobante.getStatus());
                }


                // 6. Verificar que el detalle pertenezca al comprobante
                if (detalleActual.getComprobante() == null ||
                        !detalleActual.getComprobante()
                                .getId()
                                .equals(comprobanteId)) {

                        throw new IllegalArgumentException(
                                "El detalle no pertenece al comprobante especificado.");
                }


                // 7. Validar producto
                if (detalle.getProducto() == null ||
                        detalle.getProducto().getId() == null) {

                        throw new IllegalArgumentException(
                                "El detalle debe tener un producto válido.");
                }

                Long productoId =
                        detalle.getProducto().getId();


                // 8. Buscar producto real en BD
                Optional<Producto> productoOptional =
                        productoRepository.findById(productoId);

                if (productoOptional.isEmpty()) {
                        throw new IllegalArgumentException(
                                "El producto especificado no existe.");
                }

                Producto producto =
                        productoOptional.get();


                // 9. No permitir cambiar producto
                if (!detalleActual.getProducto()
                        .getId()
                        .equals(productoId)) {

                        throw new IllegalArgumentException(
                                "No se puede cambiar el producto de un detalle existente.");
                }


                // 10. Validar talle
                if (detalle.getTalle() == null ||
                        detalle.getTalle().isBlank()) {

                        throw new IllegalArgumentException(
                                "El talle es obligatorio.");
                }

                String talle =
                        detalle.getTalle();


                // 11. No permitir cambiar talle
                if (!detalleActual.getTalle()
                        .equals(talle)) {

                        throw new IllegalArgumentException(
                                "No se puede cambiar el talle de un detalle existente.");
                }


                // 12. Validar cantidad
                if (detalle.getCantidad() == null ||
                        detalle.getCantidad() <= 0) {

                        throw new IllegalArgumentException(
                                "La cantidad debe ser mayor a cero.");
                }


                // 13. Obtener cantidades
                int cantidadActual =
                        detalleActual.getCantidad();

                int cantidadNueva =
                        detalle.getCantidad();

                int diferenciaCantidad =
                        cantidadNueva - cantidadActual;


                // 14. Buscar stock del producto + talle
                Optional<StockTalle> stockOptional =
                        stockTalleRepository.findByProductoIdAndTalle(
                                productoId,
                                talle
                        );

                if (stockOptional.isEmpty()) {
                        throw new IllegalArgumentException(
                                "No existe stock para el producto y talle indicado.");
                }

                StockTalle stockTalle =
                        stockOptional.get();


                // 15. Validar stock solamente si aumenta la cantidad
                if (diferenciaCantidad > 0 &&
                        stockTalle.getStock() < diferenciaCantidad) {

                        throw new IllegalArgumentException(
                                "No hay stock suficiente. Stock disponible: "
                                        + stockTalle.getStock()
                                        + ". Cantidad adicional solicitada: "
                                        + diferenciaCantidad);
                }


                // 16. Calcular importe anterior
                BigDecimal importeDetalleActual =
                        detalleActual.getPrecioCompra()
                                .multiply(
                                        BigDecimal.valueOf(cantidadActual)
                                );


                // 17. Actualizar stock
                //
                // Si aumenta cantidad:
                // diferencia positiva → descuenta stock
                //
                // Si disminuye cantidad:
                // diferencia negativa → devuelve stock
                //
                stockTalle.setStock(
                        stockTalle.getStock() - diferenciaCantidad
                );

                stockTalleRepository.save(stockTalle);


                // 18. Actualizar EL OBJETO QUE EXISTE EN BD
                //
                // Esto es importante.
                // No guardamos "detalle", porque viene del RequestBody
                // y puede no tener ID.
                //
                detalleActual.setCantidad(cantidadNueva);

                detalleActual.setComprobante(comprobante);

                detalleActual.setProducto(producto);

                detalleActual.setTalle(talle);


                // Mantener el precio original
                detalleActual.setPrecioCompra(
                        detalleActual.getPrecioCompra()
                );


                // 19. Guardar detalle existente
                ComprobanteDetalle detalleGuardado =
                        comprobanteDetalleRespository.save(detalleActual);


                // 20. Calcular importe nuevo
                BigDecimal importeDetalleNuevo =
                        detalleGuardado.getPrecioCompra()
                                .multiply(
                                        BigDecimal.valueOf(cantidadNueva)
                                );


                // 21. Calcular diferencia
                BigDecimal diferenciaImporte =
                        importeDetalleNuevo
                                .subtract(importeDetalleActual);


                // 22. Actualizar total del comprobante
                comprobante.setTotal(
                        comprobante.getTotal()
                                .add(diferenciaImporte)
                );


                // 23. Guardar comprobante
                comprobanteRepository.save(comprobante);


                // 24. Retornar detalle actualizado
                return detalleGuardado;
        }
}
