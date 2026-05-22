package com.corporate.luxury.luxury_corporate.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.corporate.luxury.luxury_corporate.model.Usuario;
import com.corporate.luxury.luxury_corporate.model.Usuario.Estado;
import com.corporate.luxury.luxury_corporate.model.Usuario.Rol;
import com.corporate.luxury.luxury_corporate.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        // Puedes añadir sorting aquí si quieres, o dejarlo para el repositorio.
        List<Usuario> lista = usuarioRepository.findAll();
        lista.sort(Comparator.comparing(Usuario::getUltimoAcceso, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Usuario::getRol)
                .thenComparing(Usuario::getNombre));
        return lista;
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre.trim());
    }

    @Transactional
    public Usuario crear(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }

        String[] partesNombre = usuario.getNombre().trim().split("\\s+");
        if (partesNombre.length < 3) {
            throw new IllegalArgumentException("El usuario debe tener al menos un nombre y dos apellidos");
        }

        if (usuario.getEmail() == null || !usuario.getEmail().trim().endsWith("@smartcorp.com")) {
            throw new IllegalArgumentException("El correo debe ser corporativo (@smartcorp.com)");
        }

        // ¡Genera el ID aquí si tu app lo necesita!
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            usuario.setId(generarId());
        }

        if (usuario.getUltimoAcceso() == null) {
            usuario.setUltimoAcceso(LocalDateTime.now());
        }
        if (usuario.getEstado() == null) {
            usuario.setEstado(Estado.ACTIVO);
        }
        usuarioRepository.save(usuario);
        return usuario;
    }

    @Transactional
    public Usuario actualizar(String id, Usuario datosActualizados) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + id));

        existente.setNombre(datosActualizados.getNombre());
        existente.setEmail(datosActualizados.getEmail());
        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isEmpty()) {
            existente.setPassword(datosActualizados.getPassword());
        }
        existente.setRol(datosActualizados.getRol());
        existente.setUbicacion(datosActualizados.getUbicacion());
        existente.setEstado(datosActualizados.getEstado());
        usuarioRepository.save(existente);
        return existente;
    }

    public void eliminar(String id) {
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public void cambiarEstado(String id, Estado nuevoEstado) {
        Usuario u = usuarioRepository.findById(id).orElse(null);
        if (u != null) {
            u.setEstado(nuevoEstado);
            usuarioRepository.save(u);
        }
    }

    public long contarTotal() {
        return usuarioRepository.count();
    }

    public long contarActivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getEstado() == Estado.ACTIVO)
                .count();
    }

    public long contarPorRol(Rol rol) {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getRol() == rol)
                .count();
    }

    public long contarOperadores() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getRol() == Rol.OPERADOR)
                .count();
    }

    @Transactional
    public int auditarUsuariosInactivos() {
        int desactivados = 0;
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);

        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usr : usuarios) {
            if (usr.getEstado() == Estado.ACTIVO) {
                if (usr.getUltimoAcceso() != null && usr.getUltimoAcceso().isBefore(hace30Dias)) {
                    usr.setEstado(Estado.INACTIVO);
                    usuarioRepository.save(usr);
                    desactivados++;
                }
            }
        }
        return desactivados;
    }

    // Si quieres mantener la lógica de ID incremental como antes:
    private static int contadorId = 4; // Cambia según tu data inicial

    private String generarId() {
        contadorId++;
        return String.format("USR-%03d", contadorId);
    }
}