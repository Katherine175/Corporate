package com.luxury.usuario.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.luxury.common.enums.EstadoRegistro;
import com.luxury.usuario.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
	private Long id;
	private String nombre;
	private String email;
	private EstadoRegistro estado;
	private LocalDateTime creadoEn;
	private Set<String> roles;

	public static UsuarioResponse from(Usuario usuario) {
		return new UsuarioResponse(
				usuario.getId(),
				usuario.getNombre(),
				usuario.getEmail(),
				usuario.getEstado(),
				usuario.getCreadoEn(),
				usuario.getUsuarioRoles().stream().map(usuarioRol -> usuarioRol.getRol().getNombre()).collect(Collectors.toSet()));
	}
}
