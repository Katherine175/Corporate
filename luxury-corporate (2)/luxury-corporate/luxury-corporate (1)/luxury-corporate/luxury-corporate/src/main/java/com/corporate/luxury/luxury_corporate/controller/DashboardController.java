package com.corporate.luxury.luxury_corporate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.corporate.luxury.luxury_corporate.model.Usuario;
import com.corporate.luxury.luxury_corporate.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private final UsuarioService usuarioService;

    public DashboardController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        // Validar que el usuario esté autenticado
        Object usuarioAutenticado = session.getAttribute("usuarioAutenticado");
        
        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        String nombre = (String) session.getAttribute("nombre");
        String rol = (String) session.getAttribute("rol");

        model.addAttribute("nombre", nombre);
        model.addAttribute("rol", rol);

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/usuarios")
    public String usuarios(HttpSession session, Model model) {
        // Validar autenticación
        if (session.getAttribute("usuarioAutenticado") == null) {
            return "redirect:/login";
        }

        String nombre = (String) session.getAttribute("nombre");
        String rol = (String) session.getAttribute("rol");

        model.addAttribute("nombre", nombre);
        model.addAttribute("rol", rol);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("totalUsuarios", usuarioService.contarTotal());
        model.addAttribute("usuariosActivos", usuarioService.contarActivos());
        model.addAttribute("totalOperadores", usuarioService.contarOperadores());
        model.addAttribute("nuevoUsuario", new Usuario());

        return "usuarios-nuevo";
    }

    @GetMapping("/consumo")
    public String telemetria(HttpSession session, Model model) {
        // Validar autenticación
        if (session.getAttribute("usuarioAutenticado") == null) {
            return "redirect:/login";
        }

        String nombre = (String) session.getAttribute("nombre");
        String rol = (String) session.getAttribute("rol");

        model.addAttribute("nombre", nombre);
        model.addAttribute("rol", rol);

        return "telemetria-nuevo";
    }
}
