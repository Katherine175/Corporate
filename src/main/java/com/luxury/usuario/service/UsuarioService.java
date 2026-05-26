package com.luxury.usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxury.common.exception.ResourceNotFoundException;
import com.luxury.usuario.dto.UsuarioResponse;
import com.luxury.usuario.model.Usuario;
import com.luxury.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<UsuarioResponse> listar() {
		return usuarioRepository.findAll().stream().map(UsuarioResponse::from).toList();
	}

	public UsuarioResponse obtener(Long id) {
		return UsuarioResponse.from(buscar(id));
	}

	public Usuario buscar(Long id) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
		if (usuarioOptional.isPresent()) {
			return usuarioOptional.get();
		}
		throw new ResourceNotFoundException("Usuario no encontrado");
	}
	
}
