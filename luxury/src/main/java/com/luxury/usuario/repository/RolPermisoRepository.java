package com.luxury.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxury.usuario.model.RolPermiso;
import com.luxury.usuario.model.RolPermisoId;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {
}
