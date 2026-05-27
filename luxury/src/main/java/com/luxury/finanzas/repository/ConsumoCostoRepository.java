package com.luxury.finanzas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxury.finanzas.model.ConsumoCosto;

public interface ConsumoCostoRepository extends JpaRepository<ConsumoCosto, Long> {

	List<ConsumoCosto> findByConsumoId(Long consumoId);
}
