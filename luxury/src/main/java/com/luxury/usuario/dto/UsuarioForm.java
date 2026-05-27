package com.luxury.usuario.dto;

import lombok.Data;

import com.luxury.common.enums.EstadoRegistro;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UsuarioForm {

	@NotBlank
	private String nombre;

	@NotBlank
	@Email
	private String email;

	@Size(min = 6)
	private String password;

	private String rol = "ANALISTA";

	private EstadoRegistro estado = EstadoRegistro.ACTIVO;
}
