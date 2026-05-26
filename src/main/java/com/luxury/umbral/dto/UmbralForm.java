package com.luxury.umbral.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.luxury.common.enums.EstadoRegistro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class UmbralForm {

	@NotNull
	private Long sedeId;

	@NotNull
	private Long tipoRecursoId;

	@PositiveOrZero
	private BigDecimal limiteConsumo;

	@PositiveOrZero
	private BigDecimal limitePresupuestoPen;

	@NotNull
	private LocalDate fechaInicio = LocalDate.now();

	private LocalDate fechaFin;

	private EstadoRegistro estado = EstadoRegistro.ACTIVO;

	public UmbralRequest toRequest() {
		return new UmbralRequest(sedeId, tipoRecursoId, limiteConsumo, limitePresupuestoPen, fechaInicio, fechaFin, estado);
	}
}
