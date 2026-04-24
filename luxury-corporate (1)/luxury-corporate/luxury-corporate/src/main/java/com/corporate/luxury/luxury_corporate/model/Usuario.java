package com.corporate.luxury.luxury_corporate.model;

import java.time.LocalDateTime;

public class Usuario {

    private String id;
    private String nombre;
    private String email;
    private Rol rol;
    private String ubicacion;
    private LocalDateTime ultimoAcceso;
    private Estado estado;

    public enum Rol {
        ADMINISTRADOR("Administrador"),
        SUPERVISOR("Supervisor"),
        OPERADOR("Operador");

        private final String label;

        Rol(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum Estado {
        ACTIVO("Activo"),
        INACTIVO("Inactivo");

        private final String label;

        Estado(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public Usuario() {}

    public Usuario(String id, String nombre, String email, Rol rol, String ubicacion, LocalDateTime ultimoAcceso, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.ubicacion = ubicacion;
        this.ultimoAcceso = ultimoAcceso;
        this.estado = estado;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getRolLabel() { return rol != null ? rol.getLabel() : ""; }
    public String getEstadoLabel() { return estado != null ? estado.getLabel() : ""; }
    public boolean isActivo() { return estado == Estado.ACTIVO; }
    public boolean isAdmin() { return rol == Rol.ADMINISTRADOR; }

    public String getFechaFormateada() {
        if (ultimoAcceso == null) return "";
        return ultimoAcceso.toLocalDate().toString();
    }

    public String getHoraFormateada() {
        if (ultimoAcceso == null) return "";
        return String.format("%02d:%02d:%02d",
                ultimoAcceso.getHour(),
                ultimoAcceso.getMinute(),
                ultimoAcceso.getSecond());
    }
}
