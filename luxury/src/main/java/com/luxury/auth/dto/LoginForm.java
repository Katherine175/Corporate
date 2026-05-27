package com.luxury.auth.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginForm {

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;
}
