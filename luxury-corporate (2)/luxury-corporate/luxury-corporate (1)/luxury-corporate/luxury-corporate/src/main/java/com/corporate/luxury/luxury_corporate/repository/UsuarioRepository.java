package com.corporate.luxury.luxury_corporate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corporate.luxury.luxury_corporate.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre(String nombre);
}
