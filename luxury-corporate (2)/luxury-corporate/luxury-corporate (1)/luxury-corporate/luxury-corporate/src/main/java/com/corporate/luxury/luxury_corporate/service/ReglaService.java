package com.corporate.luxury.luxury_corporate.service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.corporate.luxury.luxury_corporate.model.Regla;
import com.corporate.luxury.luxury_corporate.repository.ReglaRepository;

import jakarta.transaction.Transactional;

@Service
public class ReglaService {

    private final ReglaRepository reglaRepository;

    public ReglaService(ReglaRepository reglaRepository) {
        this.reglaRepository = reglaRepository;
    }

    public List<Regla> listarTodas() {
        List<Regla> lista = reglaRepository.findAll();
        lista.sort(Comparator.comparing(Regla::getId));
        return lista;
    }

    @Transactional
    public void crear(Regla regla) {
        // ID autogenerado similar al esquema anterior
        String nuevoId = generarId();
        regla.setId(nuevoId);
        regla.setActiva(true);
        reglaRepository.save(regla);
    }

    @Transactional
    public void actualizar(Regla regla) {
        Regla existente = reglaRepository.findById(regla.getId())
                .orElseThrow(() -> new NoSuchElementException("Regla no encontrada: " + regla.getId()));
        existente.setNombre(regla.getNombre());
        existente.setTipo(regla.getTipo());
        existente.setUmbral(regla.getUmbral());
        existente.setAccion(regla.getAccion());
        // No modificamos ID ni activa aquí a menos que lo pidas
        reglaRepository.save(existente);
    }

    @Transactional
    public void eliminar(String id) {
        reglaRepository.deleteById(id);
    }

    // Lógica de ID incremental (ajusta si cambias la convención)
    private static int contadorId = 4;
    private String generarId() {
        return String.format("R-00%d", contadorId++);
    }
}