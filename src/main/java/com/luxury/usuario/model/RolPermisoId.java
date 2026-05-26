package com.luxury.usuario.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RolPermisoId implements Serializable {

	@Column(name = "id_rol")
	private Long rolId;

	@Column(name = "id_permiso")
	private Long permisoId;
}
