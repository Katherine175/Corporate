package com.luxury.consumo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxury.alerta.service.ReglasAlertasService;
import com.luxury.auditoria.service.AuditoriaService;
import com.luxury.common.exception.ResourceNotFoundException;
import com.luxury.consumo.dto.ConsumoRequest;
import com.luxury.consumo.dto.ConsumoResponse;
import com.luxury.consumo.model.Consumo;
import com.luxury.consumo.repository.ConsumoRepository;
import com.luxury.finanzas.model.ConsumoCosto;
import com.luxury.finanzas.service.ConversionFinancieraService;
import com.luxury.recurso.service.TipoRecursoService;
import com.luxury.security.service.AuthenticatedUserService;
import com.luxury.sede.service.SedeService;
import com.luxury.tarifa.model.TarifaRecurso;
import com.luxury.tarifa.service.TarifaService;
import com.luxury.usuario.model.Usuario;

@Service
public class ConsumoService {

	@Autowired
	private ConsumoRepository consumoRepository;

	@Autowired
	private SedeService sedeService;

	@Autowired
	private TipoRecursoService tipoRecursoService;

	@Autowired
	private TarifaService tarifaService;

	@Autowired
	private ConversionFinancieraService conversionFinancieraService;

	@Autowired
	private ReglasAlertasService reglasAlertasService;

	@Autowired
	private AuditoriaService auditoriaService;

	@Autowired
	private AuthenticatedUserService authenticatedUserService;

	public ConsumoResponse registrar(ConsumoRequest request) {
		Usuario usuario = authenticatedUserService.actual();
		TarifaRecurso tarifa = tarifaService.buscarVigente(request.getSedeId(), request.getTipoRecursoId(), request.getFechaConsumo());
		Consumo consumo = new Consumo();
		consumo.setSede(sedeService.buscar(request.getSedeId()));
		consumo.setTipoRecurso(tipoRecursoService.buscar(request.getTipoRecursoId()));
		consumo.setTarifa(tarifa);
		consumo.setUsuarioRegistro(usuario);
		consumo.setCantidadConsumida(request.getCantidadConsumida());
		consumo.setFechaConsumo(request.getFechaConsumo());
		consumo.setPeriodo(request.getPeriodo());
		Consumo saved = consumoRepository.save(consumo);
		BigDecimal costoPen = request.getCantidadConsumida()
				.multiply(tarifa.getPrecioUnitarioPen())
				.setScale(4, RoundingMode.HALF_UP);
		List<ConsumoCosto> costos = conversionFinancieraService.calcularYGuardarCostos(saved, costoPen);
		reglasAlertasService.evaluarYGenerar(saved, costoPen);
		auditoriaService.registrar(usuario, "CONSUMOS", "CREAR", "consumos", saved.getId(),
				"Registro de consumo para sede " + saved.getSede().getNombre());
		return ConsumoResponse.from(saved, costos);
	}

	public List<ConsumoResponse> listar() {
		return consumoRepository.findAll().stream().map(this::toResponse).toList();
	}

	public ConsumoResponse obtener(Long id) {
		return toResponse(buscar(id));
	}

	public List<ConsumoResponse> listarPorSede(Long sedeId) {
		return consumoRepository.findBySedeId(sedeId).stream().map(this::toResponse).toList();
	}

	public List<ConsumoResponse> listarPorPeriodo(String periodo) {
		return consumoRepository.findByPeriodo(periodo).stream().map(this::toResponse).toList();
	}

	private Consumo buscar(Long id) {
		Optional<Consumo> consumoOptional = consumoRepository.findById(id);
		if (consumoOptional.isPresent()) {
			return consumoOptional.get();
		}
		throw new ResourceNotFoundException("Consumo no encontrado");
	}

	private ConsumoResponse toResponse(Consumo consumo) {
		return ConsumoResponse.from(consumo, conversionFinancieraService.listarCostos(consumo.getId()));
	}
}
