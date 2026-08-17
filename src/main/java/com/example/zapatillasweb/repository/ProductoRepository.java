package com.example.zapatillasweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.zapatillasweb.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    
}
