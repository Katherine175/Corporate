package com.corporate.luxury.luxury_corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.corporate.luxury.luxury_corporate.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email, @RequestParam String rol, HttpSession session, RedirectAttributes redirectAttributes) {
        // Validar que el usuario existe en la BD con ese email
        var usuarioOptional = usuarioService.buscarPorEmail(email);

        if (usuarioOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado. Verifica tu correo.");
            return "redirect:/login";
        }

        var usuario = usuarioOptional.get();

        // Validar que el rol coincida
        if (!usuario.getRol().name().equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "El rol no coincide con tu usuario.");
            return "redirect:/login";
        }

        // Guardar en sesión y redirigir al dashboard
        session.setAttribute("usuarioAutenticado", usuario);
        session.setAttribute("email", email);
        session.setAttribute("rol", rol);
        session.setAttribute("nombre", usuario.getNombre());

        return "redirect:/dashboard";
    }

}
