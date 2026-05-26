package com.luxury.common.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.luxury.common.enums.EstadoRegistro;
import com.luxury.finanzas.model.Moneda;
import com.luxury.finanzas.model.TipoCambio;
import com.luxury.finanzas.repository.MonedaRepository;
import com.luxury.finanzas.repository.TipoCambioRepository;
import com.luxury.recurso.model.TipoRecurso;
import com.luxury.recurso.repository.TipoRecursoRepository;
import com.luxury.sede.model.Sede;
import com.luxury.sede.repository.SedeRepository;
import com.luxury.tarifa.model.TarifaRecurso;
import com.luxury.tarifa.repository.TarifaRecursoRepository;
import com.luxury.umbral.model.Umbral;
import com.luxury.umbral.repository.UmbralRepository;
import com.luxury.usuario.model.Permiso;
import com.luxury.usuario.model.Rol;
import com.luxury.usuario.model.RolPermiso;
import com.luxury.usuario.model.RolPermisoId;
import com.luxury.usuario.model.Usuario;
import com.luxury.usuario.model.UsuarioRol;
import com.luxury.usuario.model.UsuarioRolId;
import com.luxury.usuario.repository.PermisoRepository;
import com.luxury.usuario.repository.RolPermisoRepository;
import com.luxury.usuario.repository.RolRepository;
import com.luxury.usuario.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner seedData(SeedService seedService) {
		return args -> seedService.seed();
	}

	@Configuration
	static class SeedService {

		@Autowired
		private RolRepository rolRepository;

		@Autowired
		private PermisoRepository permisoRepository;

		@Autowired
		private RolPermisoRepository rolPermisoRepository;

		@Autowired
		private UsuarioRepository usuarioRepository;

		@Autowired
		private PasswordEncoder passwordEncoder;

		@Autowired
		private MonedaRepository monedaRepository;

		@Autowired
		private TipoRecursoRepository tipoRecursoRepository;

		@Autowired
		private SedeRepository sedeRepository;

		@Autowired
		private TipoCambioRepository tipoCambioRepository;

		@Autowired
		private TarifaRecursoRepository tarifaRepository;

		@Autowired
		private UmbralRepository umbralRepository;

		void seed() {
			List<String> permisos = List.of("CONSUMO_CREAR", "CONSUMO_LISTAR", "REPORTE_VER", "USUARIO_ADMINISTRAR",
					"ALERTA_VER", "AUDITORIA_VER", "UMBRAL_ADMINISTRAR", "TARIFA_ADMINISTRAR");
			permisos.forEach(this::permiso);
			List<String> roles = List.of("ADMIN", "GERENTE", "ANALISTA", "AUDITOR");
			roles.forEach(this::rol);
			asignarPermisos("ADMIN", Set.copyOf(permisos));
			asignarPermisos("GERENTE", Set.of("CONSUMO_LISTAR", "REPORTE_VER", "ALERTA_VER"));
			asignarPermisos("ANALISTA", Set.of("CONSUMO_CREAR", "CONSUMO_LISTAR", "ALERTA_VER"));
			asignarPermisos("AUDITOR", Set.of("REPORTE_VER", "AUDITORIA_VER"));
			usuarioAdmin();
			monedas();
			recursos();
			sedes();
			tiposCambio();
			tarifasYUmbrales();
		}

		private Rol rol(String nombre) {
			return rolRepository.findByNombre(nombre).orElseGet(() -> {
				Rol rol = new Rol();
				rol.setNombre(nombre);
				return rolRepository.save(rol);
			});
		}

		private Permiso permiso(String nombre) {
			return permisoRepository.findByNombre(nombre).orElseGet(() -> {
				Permiso permiso = new Permiso();
				permiso.setNombre(nombre);
				return permisoRepository.save(permiso);
			});
		}

		private void asignarPermisos(String rolNombre, Set<String> permisos) {
			Rol rol = rol(rolNombre);
			permisos.forEach(nombre -> {
				Permiso permiso = permiso(nombre);
				RolPermisoId id = new RolPermisoId(rol.getId(), permiso.getId());
				if (!rolPermisoRepository.existsById(id)) {
					RolPermiso rolPermiso = new RolPermiso();
					rolPermiso.setId(id);
					rolPermiso.setRol(rol);
					rolPermiso.setPermiso(permiso);
					rolPermisoRepository.save(rolPermiso);
				}
			});
		}

		private void usuarioAdmin() {
			if (usuarioRepository.existsByEmail("admin@luxury.com")) {
				return;
			}
			Usuario usuario = new Usuario();
			usuario.setNombre("Administrador Luxury");
			usuario.setEmail("admin@luxury.com");
			usuario.setPassword(passwordEncoder.encode("admin123"));
			Usuario saved = usuarioRepository.save(usuario);
			Rol admin = rol("ADMIN");
			UsuarioRol usuarioRol = new UsuarioRol();
			usuarioRol.setId(new UsuarioRolId(saved.getId(), admin.getId()));
			usuarioRol.setUsuario(saved);
			usuarioRol.setRol(admin);
			saved.getUsuarioRoles().add(usuarioRol);
		}

		private void monedas() {
			Map.of("PEN", "Sol peruano", "USD", "Dolar americano", "EUR", "Euro")
					.forEach((codigo, nombre) -> monedaRepository.findByCodigo(codigo).orElseGet(() -> {
						Moneda moneda = new Moneda();
						moneda.setCodigo(codigo);
						moneda.setNombre(nombre);
						return monedaRepository.save(moneda);
					}));
		}

		private void recursos() {
			Map.of("Luz", "kWh", "Agua", "m3", "Gas", "m3")
					.forEach((nombre, unidad) -> tipoRecursoRepository.findByNombre(nombre).orElseGet(() -> {
						TipoRecurso tipo = new TipoRecurso();
						tipo.setNombre(nombre);
						tipo.setUnidadMedida(unidad);
						return tipoRecursoRepository.save(tipo);
					}));
		}

		private void sedes() {
			if (sedeRepository.count() > 0) {
				return;
			}
			crearSede("Sede Lima", "Lima", "Av. Principal 100");
			crearSede("Sede Piura", "Piura", "Av. Grau 200");
			crearSede("Sede Trujillo", "Trujillo", "Av. Espana 300");
		}

		private void tiposCambio() {
			if (tipoCambioRepository.count() > 0) {
				return;
			}
			crearTipoCambio("PEN", "USD", new BigDecimal("0.27"));
			crearTipoCambio("PEN", "EUR", new BigDecimal("0.25"));
		}

		private void tarifasYUmbrales() {
			if (tarifaRepository.count() > 0) {
				return;
			}
			Sede lima = sedeRepository.findAll().getFirst();
			tipoRecursoRepository.findAll().forEach(recurso -> {
				TarifaRecurso tarifa = new TarifaRecurso();
				tarifa.setSede(lima);
				tarifa.setTipoRecurso(recurso);
				tarifa.setPrecioUnitarioPen(new BigDecimal("0.85"));
				tarifa.setFechaInicio(LocalDate.of(2026, 1, 1));
				tarifaRepository.save(tarifa);

				Umbral umbral = new Umbral();
				umbral.setSede(lima);
				umbral.setTipoRecurso(recurso);
				umbral.setLimiteConsumo(new BigDecimal("1000"));
				umbral.setLimitePresupuestoPen(new BigDecimal("800"));
				umbral.setFechaInicio(LocalDate.of(2026, 1, 1));
				umbral.setEstado(EstadoRegistro.ACTIVO);
				umbralRepository.save(umbral);
			});
		}

		private void crearSede(String nombre, String ciudad, String direccion) {
			Sede sede = new Sede();
			sede.setNombre(nombre);
			sede.setCiudad(ciudad);
			sede.setDireccion(direccion);
			sedeRepository.save(sede);
		}

		private void crearTipoCambio(String origen, String destino, BigDecimal valor) {
			Optional<Moneda> monedaOrigenOptional = monedaRepository.findByCodigo(origen);
			Optional<Moneda> monedaDestinoOptional = monedaRepository.findByCodigo(destino);
			if (monedaOrigenOptional.isEmpty() || monedaDestinoOptional.isEmpty()) {
				return;
			}
			TipoCambio tipoCambio = new TipoCambio();
			tipoCambio.setMonedaOrigen(monedaOrigenOptional.get());
			tipoCambio.setMonedaDestino(monedaDestinoOptional.get());
			tipoCambio.setValor(valor);
			tipoCambio.setFecha(LocalDate.of(2026, 1, 1));
			tipoCambioRepository.save(tipoCambio);
		}
	}
}
