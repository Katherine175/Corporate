package com.luxury.recurso.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class TipoRecursoForm {

	@NotBlank
	private String nombre;

	@NotBlank
	private String unidadMedida;

	public TipoRecursoRequest toRequest() {
		return new TipoRecursoRequest(nombre, unidadMedida);
	}
}
