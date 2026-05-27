package com.luxury.sede.dto;

import com.luxury.common.enums.EstadoRegistro;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SedeRequest {

	@NotBlank
	private String nombre;

	@NotBlank
	private String ciudad;

	@NotBlank
	private String direccion;

	private EstadoRegistro estado;
}
