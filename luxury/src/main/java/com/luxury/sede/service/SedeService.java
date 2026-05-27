package com.luxury.sede.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxury.common.enums.EstadoRegistro;
import com.luxury.common.exception.ResourceNotFoundException;
import com.luxury.sede.dto.SedeRequest;
import com.luxury.sede.dto.SedeResponse;
import com.luxury.sede.model.Sede;
import com.luxury.sede.repository.SedeRepository;

@Service
public class SedeService {

	@Autowired
	private SedeRepository sedeRepository;

	public List<SedeResponse> listar() {
		return sedeRepository.findAll().stream().map(SedeResponse::from).toList();
	}

	public SedeResponse obtener(Long id) {
		return SedeResponse.from(buscar(id));
	}

	public SedeResponse crear(SedeRequest request) {
		Sede sede = new Sede();
		aplicar(sede, request);
		return SedeResponse.from(sedeRepository.save(sede));
	}

	public SedeResponse actualizar(Long id, SedeRequest request) {
		Sede sede = buscar(id);
		aplicar(sede, request);
		return SedeResponse.from(sedeRepository.save(sede));
	}

	public void eliminar(Long id) {
		Sede sede = buscar(id);
		sede.setEstado(EstadoRegistro.INACTIVO);
		sedeRepository.save(sede);
	}

	public Sede buscar(Long id) {
		Optional<Sede> sedeOptional = sedeRepository.findById(id);
		if (sedeOptional.isPresent()) {
			return sedeOptional.get();
		}
		throw new ResourceNotFoundException("Sede no encontrada");
	}

	private void aplicar(Sede sede, SedeRequest request) {
		sede.setNombre(request.getNombre());
		sede.setCiudad(request.getCiudad());
		sede.setDireccion(request.getDireccion());
		sede.setEstado(request.getEstado() == null ? EstadoRegistro.ACTIVO : request.getEstado());
	}
}
