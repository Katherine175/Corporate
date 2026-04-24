package com.corporate.luxury.luxury_corporate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Regla {
    private String id;
    private String nombre;
    private String tipo; 
    private int umbral;
    private String accion;
    private boolean activa;

    
    public String getTipoClass() {
        if (tipo == null) return "";
        return tipo.toLowerCase().replace("í", "i");
    }
}
