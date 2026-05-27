package com.luxury.alerta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luxury.alerta.dto.AlertaResponse;
import com.luxury.alerta.service.ReglasAlertasService;

@Controller
@RequestMapping("/alertas")
public class AlertaController {

	@Autowired
	private ReglasAlertasService reglasAlertasService;

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("alertas", reglasAlertasService.listar().stream().map(AlertaResponse::from).toList());
		return "alertas/lista";
	}

	@GetMapping("/sede/{idSede}")
	public String listarPorSede(@PathVariable Long idSede, Model model) {
		model.addAttribute("alertas",
				reglasAlertasService.listarPorSede(idSede).stream().map(AlertaResponse::from).toList());
		return "alertas/lista";
	}

	@PostMapping("/{id}/atender")
	public String atender(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		reglasAlertasService.atender(id);
		redirectAttributes.addFlashAttribute("successMessage", "Alerta atendida correctamente.");
		return "redirect:/alertas";
	}
}
