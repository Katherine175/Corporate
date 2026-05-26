package com.luxury.usuario.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "rol_permisos")
public class RolPermiso {

	@EmbeddedId
	@EqualsAndHashCode.Include
	@ToString.Include
	private RolPermisoId id = new RolPermisoId();

	@ManyToOne
	@MapsId("rolId")
	@JoinColumn(name = "id_rol")
	private Rol rol;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("permisoId")
	@JoinColumn(name = "id_permiso")
	private Permiso permiso;

	public RolPermiso(Rol rol, Permiso permiso) {
		this.rol = rol;
		this.permiso = permiso;
		this.id = new RolPermisoId(rol.getId(), permiso.getId());
	}
}
