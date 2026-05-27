package com.luxury.usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luxury.common.enums.EstadoRegistro;
import com.luxury.common.exception.BusinessException;
import com.luxury.common.exception.ResourceNotFoundException;
import com.luxury.usuario.dto.UsuarioForm;
import com.luxury.usuario.dto.UsuarioResponse;
import com.luxury.usuario.model.Rol;
import com.luxury.usuario.model.Usuario;
import com.luxury.usuario.model.UsuarioRol;
import com.luxury.usuario.model.UsuarioRolId;
import com.luxury.usuario.repository.RolRepository;
import com.luxury.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<UsuarioResponse> listar(String estado) {
		return usuarioRepository.findAll().stream()
				.filter(u -> estado == null || estado.isBlank() || (u.getEstado() != null && u.getEstado().name().equalsIgnoreCase(estado)))
				.map(UsuarioResponse::from).toList();
	}

	public UsuarioResponse obtener(Long id) {
		return UsuarioResponse.from(buscar(id));
	}

	public UsuarioResponse crear(UsuarioForm form) {
		if (usuarioRepository.existsByEmail(form.getEmail())) {
			throw new BusinessException("El email ya esta registrado");
		}
		Usuario usuario = new Usuario();
		usuario.setNombre(form.getNombre());
		usuario.setEmail(form.getEmail());
		usuario.setPassword(passwordEncoder.encode(form.getPassword()));
		usuario.setEstado(form.getEstado() == null ? EstadoRegistro.ACTIVO : form.getEstado());
		Usuario saved = usuarioRepository.save(usuario);
		asignarRol(saved, form.getRol());
		return UsuarioResponse.from(usuarioRepository.save(saved));
	}

	public UsuarioResponse actualizar(Long id, UsuarioForm form) {
		Usuario usuario = buscar(id);
		usuario.setNombre(form.getNombre());
		if (!usuario.getEmail().equals(form.getEmail()) && usuarioRepository.existsByEmail(form.getEmail())) {
			throw new BusinessException("El email ya esta registrado");
		}
		usuario.setEmail(form.getEmail());
		if (form.getPassword() != null && !form.getPassword().isBlank()) {
			usuario.setPassword(passwordEncoder.encode(form.getPassword()));
		}
		usuario.setEstado(form.getEstado() == null ? EstadoRegistro.ACTIVO : form.getEstado());
		usuario.getUsuarioRoles().clear();
		Usuario saved = usuarioRepository.save(usuario);
		asignarRol(saved, form.getRol());
		return UsuarioResponse.from(usuarioRepository.save(saved));
	}

	public void toggleEstado(Long id) {
		Usuario usuario = buscar(id);
		if (usuario.getEstado() == EstadoRegistro.ACTIVO) {
			usuario.setEstado(EstadoRegistro.INACTIVO);
		} else {
			usuario.setEstado(EstadoRegistro.ACTIVO);
		}
		usuarioRepository.save(usuario);
	}

	public Usuario buscar(Long id) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
		if (usuarioOptional.isPresent()) {
			return usuarioOptional.get();
		}
		throw new ResourceNotFoundException("Usuario no encontrado");
	}

	private void asignarRol(Usuario usuario, String roleName) {
		if (roleName == null || roleName.isBlank()) {
			roleName = "ANALISTA";
		}
		Optional<Rol> rolOptional = rolRepository.findByNombre(roleName);
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

	public List<Rol> listarRoles() {
		return rolRepository.findAll();
	}
}
