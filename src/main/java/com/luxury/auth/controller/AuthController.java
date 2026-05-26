package com.luxury.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luxury.auth.dto.LoginForm;
import com.luxury.auth.dto.RegisterForm;
import com.luxury.auth.service.AuthService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

	@Autowired
	private AuthService authService;

	@GetMapping("/login")
	public String mostrarLogin(Model model) {
		if (!model.containsAttribute("loginForm")) {
			model.addAttribute("loginForm", new LoginForm());
		}
		return "auth/login";
	}

	@GetMapping("/registro")
	public String mostrarRegistro(Model model) {
		if (!model.containsAttribute("registerForm")) {
			model.addAttribute("registerForm", new RegisterForm());
		}
		return "auth/registro";
	}

	@PostMapping("/registro")
	public String registrar(
			@Valid @ModelAttribute("registerForm") RegisterForm formulario,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "auth/registro";
		}
		authService.register(formulario.toRequest());
		redirectAttributes.addFlashAttribute("successMessage", "Usuario registrado correctamente.");
		return "redirect:/login";
	}
}
