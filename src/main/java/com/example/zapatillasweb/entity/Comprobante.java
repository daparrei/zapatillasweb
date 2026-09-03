package com.example.zapatillasweb.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "comprobantes")
public class Comprobante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime  createdAt;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "comprobante")
    private List<ComprobanteDetalle> detalles = new ArrayList<>();

    public Comprobante() {
    }

    public Comprobante(String status, LocalDateTime createdAt, BigDecimal total, Cliente cliente) {
        this.status = status;
        this.createdAt = createdAt;
        this.total = total;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    } 

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ComprobanteDetalle> getDetalles() {
    return detalles;
}

    public void setDetalles(List<ComprobanteDetalle> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Comprobante [id=" + id + ", status=" + status + ", createdAt=" + createdAt + ", total=" + total
                + ", cliente=" + cliente + "]";
    }

}
