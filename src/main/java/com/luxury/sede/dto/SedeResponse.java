package com.luxury.sede.dto;

import com.luxury.common.enums.EstadoRegistro;
import com.luxury.sede.model.Sede;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SedeResponse {

	private Long id;
	private String nombre;
	private String ciudad;
	private String direccion;
	private EstadoRegistro estado;

	public static SedeResponse from(Sede sede) {
		return new SedeResponse(sede.getId(), sede.getNombre(), sede.getCiudad(), sede.getDireccion(), sede.getEstado());
	}
}
