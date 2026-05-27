package com.luxury.finanzas.dto;

import com.luxury.finanzas.model.Moneda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonedaResponse {

	private Long id;
	private String codigo;
	private String nombre;

	public static MonedaResponse from(Moneda moneda) {
		return new MonedaResponse(moneda.getId(), moneda.getCodigo(), moneda.getNombre());
	}
}
