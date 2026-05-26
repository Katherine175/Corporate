package com.luxury.auth.dto;

import lombok.Data;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegisterForm {

	@NotBlank
	private String nombre;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(min = 6)
	private String password;

	private String rol = "ANALISTA";

	public RegisterRequest toRequest() {
		return new RegisterRequest(nombre, email, password, Set.of(rol));
	}
}
