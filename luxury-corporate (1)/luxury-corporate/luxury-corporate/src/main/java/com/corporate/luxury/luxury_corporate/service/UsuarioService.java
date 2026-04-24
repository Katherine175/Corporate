package com.corporate.luxury.luxury_corporate.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.corporate.luxury.luxury_corporate.model.Usuario;
import com.corporate.luxury.luxury_corporate.model.Usuario.Estado;
import com.corporate.luxury.luxury_corporate.model.Usuario.Rol;


@Service
public class UsuarioService {

    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();
    private int contadorId = 0;

    public UsuarioService() {
        cargarDatosIniciales();
    }


    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>(usuarios.values());
        lista.sort(Comparator.comparing(Usuario::getUltimoAcceso, Comparator.nullsLast(Comparator.reverseOrder()))
                             .thenComparing(Usuario::getRol)
                             .thenComparing(Usuario::getNombre));
        return lista;
    }

    public Optional<Usuario> buscarPorId(String id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Optional<Usuario> buscarPorNombre(String nombre) {
        return usuarios.values().stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre.trim()))
                .findFirst();
    }

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

        String id = generarId();
        usuario.setId(id);
        if (usuario.getUltimoAcceso() == null) {
            usuario.setUltimoAcceso(LocalDateTime.now());
        }
        if (usuario.getEstado() == null) {
            usuario.setEstado(Estado.ACTIVO);
        }
        usuarios.put(id, usuario);
        return usuario;
    }

    public Usuario actualizar(String id, Usuario datosActualizados) {
        Usuario existente = usuarios.get(id);
        if (existente == null) {
            throw new NoSuchElementException("Usuario no encontrado: " + id);
        }
        existente.setNombre(datosActualizados.getNombre());
        existente.setEmail(datosActualizados.getEmail());
        existente.setRol(datosActualizados.getRol());
        existente.setUbicacion(datosActualizados.getUbicacion());
        existente.setEstado(datosActualizados.getEstado());
        return existente;
    }

    public void eliminar(String id) {
        usuarios.remove(id);
    }

    public void cambiarEstado(String id, Estado nuevoEstado) {
        Usuario u = usuarios.get(id);
        if (u != null) {
            u.setEstado(nuevoEstado);
        }
    }


    public long contarTotal() {
        return usuarios.size();
    }

    public long contarActivos() {
        return usuarios.values().stream()
                .filter(u -> u.getEstado() == Estado.ACTIVO)
                .count();
    }

    public long contarPorRol(Rol rol) {
        return usuarios.values().stream()
                .filter(u -> u.getRol() == rol)
                .count();
    }

    public long contarOperadores() {
        long count = 0;
        List<Usuario> lista = new ArrayList<>(usuarios.values());
       
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getRol() == Rol.OPERADOR) {
                count++;
            }
        }
        return count;
    }

   
    public int auditarUsuariosInactivos() {
        int desactivados = 0;
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        
        
        List<Usuario> listaCompleta = new ArrayList<>(usuarios.values());
        
        for (int i = 0; i < listaCompleta.size(); i++) {
            Usuario usr = listaCompleta.get(i);
            
            
            if (usr.getEstado() == Estado.ACTIVO) {
                if (usr.getUltimoAcceso() != null && usr.getUltimoAcceso().isBefore(hace30Dias)) {
                    usr.setEstado(Estado.INACTIVO);
                    actualizar(usr.getId(), usr);
                    desactivados++;
                }
            }
        }
        
        return desactivados;
    }

   

    private String generarId() {
        contadorId++;
        return String.format("USR-%03d", contadorId);
    }

    private void cargarDatosIniciales() {
        crear(new Usuario(null, "Admin Usuario Principal", "admin@smartcorp.com",
                Rol.ADMINISTRADOR, "Todas las Sedes",
                LocalDateTime.of(2026, 4, 20, 8, 45, 23), Estado.ACTIVO));

        crear(new Usuario(null, "Operador Lima Centro", "operador@smartcorp.com",
                Rol.OPERADOR, "Sede Lima",
                LocalDateTime.of(2026, 4, 20, 8, 30, 15), Estado.ACTIVO));

        crear(new Usuario(null, "Operador Arequipa Sur", "operador.aqp@smartcorp.com",
                Rol.OPERADOR, "Sede Arequipa",
                LocalDateTime.of(2026, 4, 20, 7, 58, 42), Estado.ACTIVO));

        crear(new Usuario(null, "Supervisor Cusco Norte", "supervisor.cus@smartcorp.com",
                Rol.SUPERVISOR, "Sede Cusco",
                LocalDateTime.of(2026, 4, 19, 18, 22, 11), Estado.INACTIVO));
    }
}
