package com.corporate.luxury.luxury_corporate.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.corporate.luxury.luxury_corporate.model.Usuario;
import com.corporate.luxury.luxury_corporate.model.Usuario.Estado;
import com.corporate.luxury.luxury_corporate.service.UsuarioService;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/usuarios/crear")
    public String crearUsuario(@ModelAttribute("nuevoUsuario") Usuario nuevo,
                               RedirectAttributes redirectAttributes) {
        try {
            if (usuarioService.buscarPorEmail(nuevo.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("errorForm", "El correo " + nuevo.getEmail() + " ya está registrado.");
                return "redirect:/";
            }
            if (usuarioService.buscarPorNombre(nuevo.getNombre()).isPresent()) {
                redirectAttributes.addFlashAttribute("errorForm", "El usuario " + nuevo.getNombre() + " ya está registrado.");
                return "redirect:/";
            }
            nuevo.setEstado(Estado.ACTIVO);
            usuarioService.crear(nuevo);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorForm", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id,
                                  RedirectAttributes redirectAttributes) {
        usuarioService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado");
        return "redirect:/";
    }

    @PostMapping("/usuarios/estado/{id}")
    public String cambiarEstado(@PathVariable String id,
                                @RequestParam String estado,
                                RedirectAttributes redirectAttributes) {
        usuarioService.cambiarEstado(id, Estado.valueOf(estado));
        redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado");
        return "redirect:/";
    }

    @PostMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable String id,
                                @ModelAttribute("usuarioEditado") Usuario datos,
                                RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizar(id, datos);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorForm", e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/api/usuarios/{id}")
    @ResponseBody
    public Optional<Usuario> verUsuario(@PathVariable String id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/api/usuarios")
    @ResponseBody
    public java.util.List<Usuario> listarUsuariosApi() {
        return usuarioService.listarTodos();
    }
}
