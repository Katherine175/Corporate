package com.luxury.alerta.model;

import java.time.LocalDateTime;

import com.luxury.common.enums.EstadoAlerta;
import com.luxury.common.enums.NivelAlerta;
import com.luxury.common.enums.TipoAlerta;
import com.luxury.consumo.model.Consumo;
import com.luxury.umbral.model.Umbral;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "alertas")
public class Alerta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_alerta")
	@EqualsAndHashCode.Include
	@ToString.Include
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_consumo", nullable = false)
	private Consumo consumo;

	@ManyToOne
	@JoinColumn(name = "id_umbral", nullable = false)
	private Umbral umbral;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_alerta", nullable = false, length = 40)
	private TipoAlerta tipoAlerta;

	@Column(nullable = false, length = 300)
	private String mensaje;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private NivelAlerta nivel;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EstadoAlerta estado = EstadoAlerta.PENDIENTE;

	@Column(name = "fecha_generacion", nullable = false)
	private LocalDateTime fechaGeneracion;

	@PrePersist
	void prePersist() {
		if (fechaGeneracion == null) {
			fechaGeneracion = LocalDateTime.now();
		}
	}
}
