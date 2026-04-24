package com.corporate.luxury.luxury_corporate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.corporate.luxury.luxury_corporate.model.Usuario;
import com.corporate.luxury.luxury_corporate.service.UsuarioService;

@Controller
public class DashboardController {

    private final UsuarioService usuarioService;

    public DashboardController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

   
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("totalUsuarios", usuarioService.contarTotal());
        model.addAttribute("usuariosActivos", usuarioService.contarActivos());
        model.addAttribute("totalOperadores", usuarioService.contarOperadores());

        model.addAttribute("nuevoUsuario", new Usuario());

        return "usuarios";
    }
}
