package com.luxury.usuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luxury.usuario.dto.UsuarioForm;
import com.luxury.usuario.dto.UsuarioResponse;
import com.luxury.usuario.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public String listar(@RequestParam(required = false) String estado, Model model) {
		model.addAttribute("usuarios", usuarioService.listar(estado));
		model.addAttribute("estadoSeleccionado", estado);
		return "usuarios/lista";
	}

	@GetMapping("/{id}")
	public String obtener(@PathVariable Long id, Model model) {
		model.addAttribute("usuario", usuarioService.obtener(id));
		return "usuarios/detalle";
	}

	@GetMapping("/registrar")
	public String mostrarFormulario(Model model) {
		model.addAttribute("usuarioForm", new UsuarioForm());
		model.addAttribute("roles", usuarioService.listarRoles());
		return "usuarios/formulario";
	}

	@PostMapping
	public String crear(
			@Valid @ModelAttribute("usuarioForm") UsuarioForm formulario,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", usuarioService.listarRoles());
			return "usuarios/formulario";
		}
		usuarioService.crear(formulario);
		redirectAttributes.addFlashAttribute("successMessage", "Usuario registrado correctamente.");
		return "redirect:/usuarios";
	}

	@GetMapping("/{id}/editar")
	public String mostrarEditar(@PathVariable Long id, Model model) {
		UsuarioResponse usuario = usuarioService.obtener(id);
		UsuarioForm form = new UsuarioForm();
		form.setNombre(usuario.getNombre());
		form.setEmail(usuario.getEmail());
		form.setEstado(usuario.getEstado());
		form.setRol(usuario.getRoles().isEmpty() ? "ANALISTA" : usuario.getRoles().iterator().next());
		model.addAttribute("usuarioForm", form);
		model.addAttribute("usuarioId", id);
		model.addAttribute("roles", usuarioService.listarRoles());
		return "usuarios/formulario";
	}

	@PostMapping("/{id}/editar")
	public String actualizar(
			@PathVariable Long id,
			@Valid @ModelAttribute("usuarioForm") UsuarioForm formulario,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("usuarioId", id);
			model.addAttribute("roles", usuarioService.listarRoles());
			return "usuarios/formulario";
		}
		usuarioService.actualizar(id, formulario);
		redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado correctamente.");
		return "redirect:/usuarios";
	}

	@PostMapping("/{id}/toggle-estado")
	public String toggleEstado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		usuarioService.toggleEstado(id);
		redirectAttributes.addFlashAttribute("successMessage", "Estado de usuario actualizado correctamente.");
		return "redirect:/usuarios";
	}
}
