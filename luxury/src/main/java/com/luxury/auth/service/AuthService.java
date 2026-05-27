package com.luxury.auth.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luxury.auth.dto.RegisterRequest;
import com.luxury.common.exception.BusinessException;
import com.luxury.common.exception.ResourceNotFoundException;
import com.luxury.usuario.model.Rol;
import com.luxury.usuario.model.Usuario;
import com.luxury.usuario.model.UsuarioRol;
import com.luxury.usuario.model.UsuarioRolId;
import com.luxury.usuario.repository.RolRepository;
import com.luxury.usuario.repository.UsuarioRepository;

@Service
public class AuthService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void register(RegisterRequest request) {
		if (usuarioRepository.existsByEmail(request.getEmail())) {
			throw new BusinessException("El email ya esta registrado");
		}
		Usuario usuario = new Usuario();
		usuario.setNombre(request.getNombre());
		usuario.setEmail(request.getEmail());
		usuario.setPassword(passwordEncoder.encode(request.getPassword()));
		Usuario saved = usuarioRepository.save(usuario);
		Set<String> roles = request.getRoles() == null || request.getRoles().isEmpty() ? Set.of("ANALISTA")
				: request.getRoles();
		roles.forEach(roleName -> asignarRol(saved, roleName));
		usuarioRepository.save(saved);
	}

	private void asignarRol(Usuario usuario, String roleName) {
		java.util.Optional<Rol> rolOptional = rolRepository.findByNombre(roleName);
		if (rolOptional.isEmpty()) {
			throw new ResourceNotFoundException("Rol no encontrado: " + roleName);
		}
		Rol rol = rolOptional.get();
		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setUsuario(usuario);
		usuarioRol.setRol(rol);
		usuarioRol.setId(new UsuarioRolId(usuario.getId(), rol.getId()));
		usuario.getUsuarioRoles().add(usuarioRol);
	}
}
