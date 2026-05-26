package com.luxury.tarifa.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.luxury.common.enums.EstadoRegistro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class TarifaForm {

	@NotNull
	private Long sedeId;

	@NotNull
	private Long tipoRecursoId;

	@NotNull
	@Positive
	private BigDecimal precioUnitarioPen;

	@NotNull
	private LocalDate fechaInicio = LocalDate.now();

	private LocalDate fechaFin;

	private EstadoRegistro estado = EstadoRegistro.ACTIVO;

	public TarifaRequest toRequest() {
		return new TarifaRequest(sedeId, tipoRecursoId, precioUnitarioPen, fechaInicio, fechaFin, estado);
	}
}
