package com.corporate.luxury.luxury_corporate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.corporate.luxury.luxury_corporate.model.Consumo;
import com.corporate.luxury.luxury_corporate.service.SimulacionService;

@Controller
public class SimulacionController {

    private final SimulacionService simulacionService;

    public SimulacionController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    @GetMapping("/simulacion")
    public String index(Model model) {
        model.addAttribute("consumos", simulacionService.listarTodos());
        model.addAttribute("nuevoConsumo", new Consumo());
        model.addAttribute("granTotal", simulacionService.obtenerGranTotal());
        return "consumo";
    }

    @PostMapping("/simulacion/registrar")
    public String registrarConsumo(Consumo consumo, RedirectAttributes redirectAttributes) {
        try {
            if (consumo.getMonto() != null && consumo.getMonto() > 0) {
                simulacionService.guardarConsumo(consumo);
                redirectAttributes.addFlashAttribute("mensaje", "Consumo registrado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "El monto debe ser mayor a 0");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar");
        }
        return "redirect:/simulacion";
    }

    @PostMapping("/simulacion/simular")
    public String generarSimulacion(RedirectAttributes redirectAttributes) {
        simulacionService.generarConsumosAleatorios();
        redirectAttributes.addFlashAttribute("mensaje", "Se generaron 5 registros de prueba");
        return "redirect:/simulacion";
    }

    @PostMapping("/simulacion/limpiar")
    public String limpiarDatos(RedirectAttributes redirectAttributes) {
        simulacionService.limpiarConsumos();
        redirectAttributes.addFlashAttribute("mensaje", "Datos eliminados");
        return "redirect:/simulacion";
    }

    @PostMapping("/simulacion/editar")
    public String editarConsumo(Consumo consumo, RedirectAttributes redirectAttributes) {
        try {
            if (consumo.getId() != null && consumo.getMonto() != null && consumo.getMonto() > 0) {
                simulacionService.editarConsumo(consumo);
                redirectAttributes.addFlashAttribute("mensaje", "Consumo actualizado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Datos inválidos");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar");
        }
        return "redirect:/simulacion";
    }
}
