package com.luxury.tarifa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luxury.recurso.service.TipoRecursoService;
import com.luxury.sede.service.SedeService;
import com.luxury.tarifa.dto.TarifaForm;
import com.luxury.tarifa.service.TarifaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tarifas")
public class TarifaController {

	@Autowired
	private TarifaService tarifaService;

	@Autowired
	private SedeService sedeService;

	@Autowired
	private TipoRecursoService tipoRecursoService;

	@GetMapping
	public String listar(Model model) {
		agregarCatalogos(model);
		model.addAttribute("tarifas", tarifaService.listar());
		model.addAttribute("tarifaForm", new TarifaForm());
		return "tarifas/lista";
	}

	@PostMapping
	public String crear(
			@Valid @ModelAttribute("tarifaForm") TarifaForm formulario,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			agregarCatalogos(model);
			model.addAttribute("tarifas", tarifaService.listar());
			return "tarifas/lista";
		}
		tarifaService.crear(formulario.toRequest());
		redirectAttributes.addFlashAttribute("successMessage", "Tarifa registrada correctamente.");
		return "redirect:/tarifas";
	}

	private void agregarCatalogos(Model model) {
		model.addAttribute("sedes", sedeService.listar());
		model.addAttribute("tiposRecurso", tipoRecursoService.listar());
	}
}
