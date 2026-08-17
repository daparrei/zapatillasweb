package com.example.zapatillasweb.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_talles")


public class StockTalle {
    
    public StockTalle() {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "talle", nullable = false)
    private String talle;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private Producto producto;

    public StockTalle(String talle, Integer stock, Producto producto) {
        this.talle = talle;
        this.stock = stock;
        this.producto = producto;
    }


    public Long getId() {
        return id;
    }


    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    @Override
    public String toString() {
        return "StockTalle [id=" + id + ", talle=" + talle + ", stock=" + stock + "]";
    }


}
