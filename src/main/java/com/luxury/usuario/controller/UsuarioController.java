package com.luxury.usuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luxury.usuario.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("usuarios", usuarioService.listar());
		return "usuarios/lista";
	}

	@GetMapping("/{id}")
	public String obtener(@PathVariable Long id, Model model) {
		model.addAttribute("usuario", usuarioService.obtener(id));
		return "usuarios/detalle";
	}
}
