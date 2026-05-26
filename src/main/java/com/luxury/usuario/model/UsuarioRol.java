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
@Table(name = "usuario_roles")
public class UsuarioRol {

	@EmbeddedId
	@EqualsAndHashCode.Include
	@ToString.Include
	private UsuarioRolId id = new UsuarioRolId();

	@ManyToOne
	@MapsId("usuarioId")
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("rolId")
	@JoinColumn(name = "id_rol")
	private Rol rol;

	public UsuarioRol(Usuario usuario, Rol rol) {
		this.usuario = usuario;
		this.rol = rol;
		this.id = new UsuarioRolId(usuario.getId(), rol.getId());
	}
}
