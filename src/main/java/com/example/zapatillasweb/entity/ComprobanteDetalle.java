package com.example.zapatillasweb.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;


/*id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comprobante_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    talle VARCHAR(5) NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL

    */

@Entity
@Table(name = "comprobantes_detalle")
public class ComprobanteDetalle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comprobante_id", nullable = false)
    private Comprobante comprobante;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "talle", nullable = false)
    private String talle;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCompra;

    public ComprobanteDetalle() {
    }

    public ComprobanteDetalle(Comprobante comprobante, Producto producto, String talle, Integer cantidad, BigDecimal precioCompra) {
        this.comprobante = comprobante;
        this.producto = producto;
        this.talle = talle;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
    }

    public Long getId() {
        return id;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public Producto getProducto() {
        return producto;
    }

    public String getTalle() {
        return talle;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    @Override
    public String toString() {
        return "ComprobanteDetalle{" +
                "id=" + id +
                ", comprobante=" + comprobante.getId() +
                ", producto=" + producto.getId() +
                ", talle='" + talle + '\'' +
                ", cantidad=" + cantidad +
                ", precioCompra=" + precioCompra +
                '}';
    }


}
