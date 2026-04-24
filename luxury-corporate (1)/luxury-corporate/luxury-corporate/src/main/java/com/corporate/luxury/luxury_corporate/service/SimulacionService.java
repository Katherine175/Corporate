package com.corporate.luxury.luxury_corporate.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.corporate.luxury.luxury_corporate.model.Consumo;

@Service
public class SimulacionService {

    private final List<Consumo> consumos = new ArrayList<>();

    public List<Consumo> listarTodos() {
        return consumos;
    }

    public void guardarConsumo(Consumo consumo) {
        if (consumo.getMonto() > 0) {
            consumo.setId(UUID.randomUUID().toString());
            consumo.setFecha(LocalDateTime.now());
            consumos.add(consumo);
        }
    }

    public void generarConsumosAleatorios() {
        String[] sedes = {"Lima Centro", "Lima Norte", "Arequipa", "Cusco", "Trujillo"};
        String[] recursos = {"Energía", "Agua", "Gas"};
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            String sede = sedes[random.nextInt(sedes.length)];
            String recurso = recursos[random.nextInt(recursos.length)];
            Double monto = 100.0 + (5000.0 * random.nextDouble());
            
            Consumo consumo = new Consumo();
            consumo.setId(UUID.randomUUID().toString());
            consumo.setSede(sede);
            consumo.setTipoRecurso(recurso);
            consumo.setMonto(Math.round(monto * 100.0) / 100.0);
            consumo.setFecha(LocalDateTime.now());
            
            consumos.add(consumo);
        }
    }

    public Double obtenerGranTotal() {
        return consumos.stream()
            .mapToDouble(Consumo::getMonto)
            .sum();
    }

    public void editarConsumo(Consumo consumoEditado) {
        consumos.stream()
            .filter(c -> c.getId().equals(consumoEditado.getId()))
            .findFirst()
            .ifPresent(c -> {
                c.setTipoRecurso(consumoEditado.getTipoRecurso());
                c.setMonto(consumoEditado.getMonto());
                c.setFecha(LocalDateTime.now());
            });
    }

    public void limpiarConsumos() {
        consumos.clear();
    }
}
