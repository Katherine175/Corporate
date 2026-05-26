package com.luxury;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthAndConsumoIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void muestraFormularioLogin() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("auth/login"));
	}

	@Test
	void loginCorrectoRedirigeAlDashboard() throws Exception {
		mockMvc.perform(post("/login")
				.param("email", "admin@luxury.com")
				.param("password", "admin123"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/dashboard"));
	}

	@Test
	void rutaProtegidaRedirigeALoginSinSesion() throws Exception {
		mockMvc.perform(get("/sedes"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/login"));
	}

	@Test
	@WithMockUser(username = "admin@luxury.com", roles = "ADMIN")
	void registrarConsumoPorFormularioRedirigeAlDetalle() throws Exception {
		mockMvc.perform(post("/consumos/registrar")
				.param("sedeId", "1")
				.param("tipoRecursoId", "1")
				.param("cantidadConsumida", "100")
				.param("fechaConsumo", "2026-05-01")
				.param("periodo", "2026-05"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrlPattern("/consumos/*"));
	}
}
