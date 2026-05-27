package com.luxury.consumo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxury.consumo.model.Consumo;

public interface ConsumoRepository extends JpaRepository<Consumo, Long> {

	List<Consumo> findBySedeId(Long sedeId);

	List<Consumo> findByPeriodo(String periodo);
}
