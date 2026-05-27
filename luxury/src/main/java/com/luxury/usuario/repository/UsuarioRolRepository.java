package com.luxury.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxury.usuario.model.UsuarioRol;
import com.luxury.usuario.model.UsuarioRolId;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
}
