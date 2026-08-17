package com.example.zapatillasweb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "productos")

public class Producto {
    public Producto() {
    }
 // Crear atributos para la tabla CREATE TABLE productos (    id BIGINT PRIMARY KEY,    categoria VARCHAR(50) NOT NULL,    nombre VARCHAR(100) NOT NULL, precio DECIMAL(10,2) NOT NULL, imagen VARCHAR(255), descripcion VARCHAR(500), alt VARCHAR(255));
    
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private BigDecimal  precio;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "alt")
    private String alt;

    @OneToMany (mappedBy = "producto",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private List<StockTalle> stockTalles = new ArrayList<>();
    

    public Producto(String categoria, String nombre, BigDecimal  precio, String imagen, String descripcion,
            String alt) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.alt = alt;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal  getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal  precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public List<StockTalle> getStockTalles() {
        return stockTalles;
    }

    public void setStockTalles(List<StockTalle> stockTalles) {
        this.stockTalles = stockTalles;
    }

    public void agregarStock(StockTalle stockTalle) {
        stockTalles.add(stockTalle);
        stockTalle.setProducto(this);
    }

    public void eliminarStock(StockTalle stockTalle) {
        stockTalles.remove(stockTalle);
        stockTalle.setProducto(null);
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", categoria=" + categoria + ", nombre=" + nombre + ", precio=" + precio
                + ", imagen=" + imagen + ", descripcion=" + descripcion + ", alt=" + alt + "]";
    }
}

