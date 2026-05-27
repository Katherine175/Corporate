package com.luxury.umbral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luxury.recurso.service.TipoRecursoService;
import com.luxury.sede.service.SedeService;
import com.luxury.umbral.dto.UmbralForm;
import com.luxury.umbral.service.UmbralService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/umbrales")
public class UmbralController {

	@Autowired
	private UmbralService umbralService;

	@Autowired
	private SedeService sedeService;

	@Autowired
	private TipoRecursoService tipoRecursoService;

	@GetMapping
	public String listar(Model model) {
		agregarCatalogos(model);
		model.addAttribute("umbrales", umbralService.listar());
		model.addAttribute("umbralForm", new UmbralForm());
		return "umbrales/lista";
	}

	@PostMapping
	public String crear(
			@Valid @ModelAttribute("umbralForm") UmbralForm formulario,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			agregarCatalogos(model);
			model.addAttribute("umbrales", umbralService.listar());
			return "umbrales/lista";
		}
		umbralService.crear(formulario.toRequest());
		redirectAttributes.addFlashAttribute("successMessage", "Umbral registrado correctamente.");
		return "redirect:/umbrales";
	}

	@PostMapping("/{id}/eliminar")
	public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		umbralService.eliminar(id);
		redirectAttributes.addFlashAttribute("successMessage", "Umbral desactivado correctamente.");
		return "redirect:/umbrales";
	}

	private void agregarCatalogos(Model model) {
		model.addAttribute("sedes", sedeService.listar());
		model.addAttribute("tiposRecurso", tipoRecursoService.listar());
	}
}
