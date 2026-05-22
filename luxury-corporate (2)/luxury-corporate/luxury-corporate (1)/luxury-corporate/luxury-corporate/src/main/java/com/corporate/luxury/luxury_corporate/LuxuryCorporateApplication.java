package com.corporate.luxury.luxury_corporate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.corporate.luxury.luxury_corporate.repository")
public class LuxuryCorporateApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuxuryCorporateApplication.class, args);
	}

	@Component
	public static class AppStartup {
		@EventListener(ApplicationReadyEvent.class)
		public void onApplicationReady() {
			System.out.println("✅ Aplicación iniciada y lista en http://localhost:8080");
			System.out.println("🌐 Presiona Ctrl+C para detener la aplicación");
		}
	}

}
