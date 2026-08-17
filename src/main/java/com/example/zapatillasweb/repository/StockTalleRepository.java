package com.example.zapatillasweb.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.zapatillasweb.entity.StockTalle;

public interface StockTalleRepository extends JpaRepository<StockTalle, Long> {
    
    List<StockTalle> findByProductoId(Long productoId);
    Optional<StockTalle> findByProductoIdAndTalle(Long productoId, String talle);

}
