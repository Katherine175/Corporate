package com.luxury.usuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxury.usuario.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {

	Optional<Permiso> findByNombre(String nombre);
}
