package com.luxury.sede.dto;

import lombok.Data;

import com.luxury.common.enums.EstadoRegistro;

import jakarta.validation.constraints.NotBlank;

@Data
public class SedeForm {

	@NotBlank
	private String nombre;

	@NotBlank
	private String ciudad;

	@NotBlank
	private String direccion;

	private EstadoRegistro estado = EstadoRegistro.ACTIVO;

	public SedeRequest toRequest() {
		return new SedeRequest(nombre, ciudad, direccion, estado);
	}
}
