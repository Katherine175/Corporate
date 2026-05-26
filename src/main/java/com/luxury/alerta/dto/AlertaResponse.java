package com.luxury.alerta.dto;

import java.time.LocalDateTime;

import com.luxury.alerta.model.Alerta;
import com.luxury.common.enums.EstadoAlerta;
import com.luxury.common.enums.NivelAlerta;
import com.luxury.common.enums.TipoAlerta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaResponse {
	private Long id;
	private Long consumoId;
	private String sede;
	private String tipoRecurso;
	private TipoAlerta tipoAlerta;
	private String mensaje;
	private NivelAlerta nivel;
	private EstadoAlerta estado;
	private LocalDateTime fechaGeneracion;

	public static AlertaResponse from(Alerta alerta) {
		return new AlertaResponse(
				alerta.getId(),
				alerta.getConsumo().getId(),
				alerta.getConsumo().getSede().getNombre(),
				alerta.getConsumo().getTipoRecurso().getNombre(),
				alerta.getTipoAlerta(),
				alerta.getMensaje(),
				alerta.getNivel(),
				alerta.getEstado(),
				alerta.getFechaGeneracion());
	}
}
