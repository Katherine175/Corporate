package com.corporate.luxury.luxury_corporate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reglas")
public class Regla {
    @Id
    private String id;

    private String nombre;
    private String tipo;
    private int umbral;
    private String accion;
    private boolean activa;

    // --- Constructores ---
    public Regla() {
    }

    public Regla(String id, String nombre, String tipo, int umbral, String accion, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.umbral = umbral;
        this.accion = accion;
        this.activa = activa;
    }

    // --- Getters y Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getUmbral() { return umbral; }
    public void setUmbral(int umbral) { this.umbral = umbral; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    // --- Utilidad para el badge CSS ---
    public String getTipoClass() {
        if (tipo == null) return "";
        return tipo.toLowerCase().replace("í", "i");
    }
}