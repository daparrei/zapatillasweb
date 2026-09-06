package com.example.zapatillasweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.zapatillasweb.entity.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}
