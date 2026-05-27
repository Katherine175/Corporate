package com.luxury.alerta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxury.alerta.model.Alerta;
import com.luxury.common.enums.NivelAlerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

	List<Alerta> findByConsumoSedeId(Long sedeId);

	long countByNivel(NivelAlerta nivel);
}
