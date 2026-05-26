package com.luxury.auditoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxury.auditoria.model.Auditoria;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

	List<Auditoria> findByUsuarioId(Long usuarioId);

	List<Auditoria> findByModuloIgnoreCase(String modulo);
}
