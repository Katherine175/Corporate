package com.luxury.auth.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

	@NotBlank
	private String nombre;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(min = 6)
	private String password;

	private Set<String> roles;
}
