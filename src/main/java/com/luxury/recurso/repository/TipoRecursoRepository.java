package com.luxury.recurso.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxury.recurso.model.TipoRecurso;

public interface TipoRecursoRepository extends JpaRepository<TipoRecurso, Long> {

	Optional<TipoRecurso> findByNombre(String nombre);
}
