package com.corporate.luxury.luxury_corporate.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consumo {
    
    private String id;
    private String sede;
    private String tipoRecurso;
    private Double monto;
    private LocalDateTime fecha;
}
