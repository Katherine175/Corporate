package com.corporate.luxury.luxury_corporate.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.corporate.luxury.luxury_corporate.model.Regla;

@Service
public class ReglaService {
    private final ConcurrentHashMap<String, Regla> reglas = new ConcurrentHashMap<>();
    private int contadorId = 4;

    public ReglaService() {
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        reglas.put("R-001", new Regla("R-001", "Alerta Consumo Energético Alto", "Energía", 8000, "Notificación al Admin", true));
        reglas.put("R-002", new Regla("R-002", "Límite Consumo Agua", "Agua", 350, "Bloqueo temporal", true));
        reglas.put("R-003", new Regla("R-003", "Control de Gastos Mensuales", "Costo", 50000, "Reporte automático", false));
    }

    public List<Regla> listarTodas() {
        return reglas.values().stream()
            .sorted((a, b) -> a.getId().compareTo(b.getId()))
            .collect(Collectors.toList());
    }

    public void crear(Regla regla) {
        String id = "R-00" + contadorId++;
        regla.setId(id);
        regla.setActiva(true);
        reglas.put(id, regla);
    }

    public void actualizar(Regla regla) {
        if (reglas.containsKey(regla.getId())) {
            Regla existente = reglas.get(regla.getId());
            existente.setNombre(regla.getNombre());
            existente.setTipo(regla.getTipo());
            existente.setUmbral(regla.getUmbral());
            existente.setAccion(regla.getAccion());
            // la regla editada se guarda.
        }
    }

    public void eliminar(String id) {
        reglas.remove(id);
    }
}
