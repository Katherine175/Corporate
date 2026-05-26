package com.luxury.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.luxury.common.exception.ResourceNotFoundException;
import com.luxury.usuario.model.Usuario;
import com.luxury.usuario.repository.UsuarioRepository;

@Service
public class AuthenticatedUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario actual() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getName() == null) {
			throw new ResourceNotFoundException("Usuario autenticado no encontrado");
		}
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(authentication.getName());
		if (usuarioOptional.isPresent()) {
			return usuarioOptional.get();
		}
		throw new ResourceNotFoundException("Usuario autenticado no encontrado");
	}
}
