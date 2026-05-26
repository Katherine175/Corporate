package com.luxury.security.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.luxury.common.enums.EstadoRegistro;
import com.luxury.usuario.model.Usuario;
import com.luxury.usuario.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
		if (usuarioOptional.isEmpty()) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		Usuario usuario = usuarioOptional.get();
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getUsuarioRoles().forEach(usuarioRol -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + usuarioRol.getRol().getNombre()));
			usuarioRol.getRol().getRolPermisos().forEach(rolPermiso ->
					authorities.add(new SimpleGrantedAuthority(rolPermiso.getPermiso().getNombre())));
		});
		boolean enabled = usuario.getEstado() == EstadoRegistro.ACTIVO;
		return User.withUsername(usuario.getEmail())
				.password(usuario.getPassword())
				.authorities(authorities)
				.disabled(!enabled)
				.build();
	}
}
