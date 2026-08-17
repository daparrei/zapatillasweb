package com.example.zapatillasweb.service;

import org.springframework.stereotype.Service;
import com.example.zapatillasweb.repository.StockTalleRepository;
import com.example.zapatillasweb.entity.StockTalle;
import com.example.zapatillasweb.entity.Producto;

import java.util.Optional;
import java.util.List;



@Service
public class StockTalleService {
    
    private final StockTalleRepository stockTalleRepository;
    private final ProductoService productoService;



    public StockTalleService(StockTalleRepository stockTalleRepository, ProductoService productoService) {
        this.stockTalleRepository = stockTalleRepository;
        this.productoService = productoService;
    }


    public Optional <StockTalle> obtenerStockTalle(Long id) {
        return stockTalleRepository.findById(id);
    }

    public StockTalle crearStockTalle(Long productoId, StockTalle stockTalle) {
        Producto producto = productoService.obtenerProducto(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        stockTalle.setProducto(producto);
        return stockTalleRepository.save(stockTalle);
    }

    public boolean eliminarStockTalle(Long id) {
        if (stockTalleRepository.existsById(id)) {
            stockTalleRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<StockTalle> obtenerStockTallePorProductoYTalle(Long productoId, String talle) {
        return stockTalleRepository.findByProductoIdAndTalle(productoId, talle);
    }

    // Buscar todos los talles de un producto
    public List<StockTalle> obtenerStockPorProducto(Long productoId) {
        return stockTalleRepository.findByProductoId(productoId);
    }

    // Actualizar un registro de stock talle
    public Optional<StockTalle> actualizarStock (Long productoId, String talle, Integer nuevoStock) {
        Optional<StockTalle> stockTalleOpt = stockTalleRepository.findByProductoIdAndTalle(productoId, talle);
        if (stockTalleOpt.isPresent()) {
            StockTalle stockTalle = stockTalleOpt.get();
            stockTalle.setStock(nuevoStock);
            return Optional.of(stockTalleRepository.save(stockTalle));
        } else {
            return Optional.empty();
        }
    }

    //Sumar Stock a un producto y talle
    public Optional<StockTalle> sumarStock(
            Long productoId,
            String talle,
            Integer cantidad) {

        Optional<StockTalle> stockTalle =
                stockTalleRepository.findByProductoIdAndTalle(
                        productoId, talle);

        if (stockTalle.isPresent()) {

            StockTalle stock = stockTalle.get();

            stock.setStock(stock.getStock() + cantidad);

            return Optional.of(stockTalleRepository.save(stock));
        }

        return Optional.empty();
    }

    //Restar stock siempre que no sea menor que "0"
    public Optional<StockTalle> restarStock(
        Long productID,
        String talle,
        Integer cantidad) {

        Optional<StockTalle> stockTalle =
                stockTalleRepository.findByProductoIdAndTalle(
                        productID, talle);
        if (stockTalle.isPresent()) {

            StockTalle stock = stockTalle.get();

            if (stock.getStock() - cantidad >= 0) {
                stock.setStock(stock.getStock() - cantidad);
                return Optional.of(stockTalleRepository.save(stock));
            } else {
                throw new IllegalArgumentException("No se puede restar más stock del disponible");
            }
        }
        throw new IllegalArgumentException("Stock no encontrado para el producto y talle especificados");
    }
}
