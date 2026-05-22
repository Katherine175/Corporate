package com.corporate.luxury.luxury_corporate.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "consumos")
public class Consumo {

    @Id
    private String id;

    private String sede;

    @Column(name = "tipo_recurso")
    private String tipoRecurso;

    private Double monto;

    private LocalDateTime fecha;

    // --- Constructores ---
    public Consumo() {
    }

    public Consumo(String id, String sede, String tipoRecurso, Double monto, LocalDateTime fecha) {
        this.id = id;
        this.sede = sede;
        this.tipoRecurso = tipoRecurso;
        this.monto = monto;
        this.fecha = fecha;
    }

    // --- Getters y Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }

    public String getTipoRecurso() { return tipoRecurso; }
    public void setTipoRecurso(String tipoRecurso) { this.tipoRecurso = tipoRecurso; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}