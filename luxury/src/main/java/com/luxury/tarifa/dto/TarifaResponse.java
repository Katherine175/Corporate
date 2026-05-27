package com.luxury.tarifa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.luxury.common.enums.EstadoRegistro;
import com.luxury.tarifa.model.TarifaRecurso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaResponse {
	private Long id;
	private Long sedeId;
	private String sede;
	private Long tipoRecursoId;
	private String tipoRecurso;
	private BigDecimal precioUnitarioPen;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private EstadoRegistro estado;

	public static TarifaResponse from(TarifaRecurso tarifa) {
		return new TarifaResponse(
				tarifa.getId(),
				tarifa.getSede().getId(),
				tarifa.getSede().getNombre(),
				tarifa.getTipoRecurso().getId(),
				tarifa.getTipoRecurso().getNombre(),
				tarifa.getPrecioUnitarioPen(),
				tarifa.getFechaInicio(),
				tarifa.getFechaFin(),
				tarifa.getEstado());
	}
}
