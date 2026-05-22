package com.corporate.luxury.luxury_corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corporate.luxury.luxury_corporate.model.Consumo;

public interface ConsumoRepository extends JpaRepository<Consumo, String> {
    // Puedes agregar métodos personalizados si lo requieres
}
