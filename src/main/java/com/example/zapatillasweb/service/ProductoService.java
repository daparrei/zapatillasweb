package com.example.zapatillasweb.service;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.zapatillasweb.entity.Producto;
import com.example.zapatillasweb.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Optional<Producto> obtenerProducto(Long id) {
        return productoRepository.findById(id);
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

}
