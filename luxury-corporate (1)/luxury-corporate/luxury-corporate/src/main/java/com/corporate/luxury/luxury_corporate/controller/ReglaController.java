package com.corporate.luxury.luxury_corporate.controller;

import com.corporate.luxury.luxury_corporate.model.Regla;
import com.corporate.luxury.luxury_corporate.service.ReglaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReglaController {

    private final ReglaService reglaService;

    public ReglaController(ReglaService reglaService) {
        this.reglaService = reglaService;
    }

    @GetMapping("/reglas")
    public String verReglas(Model model) {
        model.addAttribute("reglas", reglaService.listarTodas());
        model.addAttribute("nuevaRegla", new Regla());
        return "reglas";
    }

    @PostMapping("/reglas/crear")
    public String crearRegla(Regla regla, RedirectAttributes redirectAttributes) {
        reglaService.crear(regla);
        redirectAttributes.addFlashAttribute("mensaje", "Nueva regla creada con éxito.");
        return "redirect:/reglas";
    }

    @PostMapping("/reglas/eliminar")
    public String eliminarRegla(String id, RedirectAttributes redirectAttributes) {
        reglaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Regla eliminada.");
        return "redirect:/reglas";
    }

    @PostMapping("/reglas/actualizar")
    public String actualizarRegla(Regla regla, RedirectAttributes redirectAttributes) {
        reglaService.actualizar(regla);
        redirectAttributes.addFlashAttribute("mensaje", "Regla actualizada con éxito.");
        return "redirect:/reglas";
    }
}
