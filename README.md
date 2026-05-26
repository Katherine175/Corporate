# Luxury

## 1. Descripcion General

Luxury es una aplicacion web empresarial desarrollada con Spring Boot, Maven, Java 21, Spring MVC, Thymeleaf, Spring Security, Spring Data JPA, Hibernate y PostgreSQL. El sistema esta orientado al control de consumos operativos de sedes corporativas, como luz, agua y gas.

El proyecto permite administrar sedes, tipos de recurso, tarifas, monedas, tipos de cambio, umbrales, consumos, alertas, auditorias y eventos de acceso. La aplicacion trabaja con formularios web mediante `@Controller`, `Model`, `@ModelAttribute`, `BindingResult` y `RedirectAttributes`, siguiendo una estructura similar a la trabajada en clase.

El sistema no usa Angular, React ni Vue. La interfaz esta construida con plantillas Thymeleaf ubicadas en `src/main/resources/templates`.

## 2. Planteamiento Del Problema

En muchas empresas con varias sedes corporativas, el control de consumos operativos suele manejarse de forma manual o dispersa. Los registros de luz, agua y gas pueden estar en hojas de calculo, correos o documentos separados, lo que dificulta comparar consumos entre sedes, revisar costos por periodo y detectar excesos a tiempo.

Tambien existe poca trazabilidad financiera cuando los costos se calculan en distintas monedas, ya que si el tipo de cambio cambia en el futuro, no siempre queda claro que tasa se uso para calcular un consumo anterior. A esto se suma que los limites de consumo o presupuesto no siempre se controlan automaticamente, por lo que una sede puede superar un umbral sin generar una alerta inmediata.

Luxury busca resolver este problema centralizando la informacion operativa y financiera en una base de datos normalizada, permitiendo registrar consumos por sede y recurso, calcular costos en PEN, USD y EUR, generar alertas por exceso, consultar reportes y mantener auditoria de acciones importantes.

## 3. Alcance Del Sistema

El alcance actual del sistema es un backend web monolitico modular con vistas Thymeleaf. El sistema permite:

- Autenticar usuarios mediante formulario de login.
- Gestionar usuarios, roles y permisos.
- Administrar sedes corporativas.
- Administrar tipos de recurso como luz, agua y gas.
- Registrar monedas y tipos de cambio.
- Configurar tarifas por sede y recurso.
- Configurar umbrales de consumo y presupuesto.
- Registrar consumos operativos.
- Calcular costos en diferentes monedas.
- Generar alertas por exceso de consumo o presupuesto.
- Consultar dashboard y reportes mensuales.
- Revisar auditorias y eventos de acceso.

El sistema esta pensado como proyecto academico empresarial. Por eso se prioriza una estructura clara con controladores MVC, servicios, repositorios, entidades JPA, DTOs/formularios y vistas.

## 4. Stack Tecnologico

| Elemento | Tecnologia |
| --- | --- |
| Lenguaje | Java 21 |
| Framework principal | Spring Boot |
| Gestion del proyecto | Maven |
| Capa web | Spring MVC |
| Motor de plantillas | Thymeleaf |
| Seguridad | Spring Security |
| Persistencia | Spring Data JPA |
| ORM | Hibernate |
| Base de datos final | PostgreSQL |
| Base de datos para tests | H2 |
| Validaciones | Jakarta Validation |
| Reduccion de codigo | Lombok |
| Pruebas | JUnit / Spring Boot Test / MockMvc |

## 5. Tipo De Arquitectura

Luxury esta construido como un monolito modular. Esto significa que existe una sola aplicacion Spring Boot y un solo proyecto Maven, pero el codigo esta dividido internamente por modulos funcionales.

La comunicacion interna sigue esta regla:

```text
Controller -> Service -> Repository -> PostgreSQL
```

Cada modulo tiene su propio paquete con responsabilidades claras. Los controladores reciben peticiones web, los servicios contienen la logica de negocio, los repositorios acceden a la base de datos y las entidades representan las tablas.

## 6. Estructura General Del Proyecto

```text
luxury/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── HELP.md
├── README.md
├── FUNCIONALIDADES.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── luxury/
    │   │           ├── LuxuryApplication.java
    │   │           ├── alerta/
    │   │           ├── auditoria/
    │   │           ├── auth/
    │   │           ├── common/
    │   │           ├── consumo/
    │   │           ├── dashboard/
    │   │           ├── eventoacceso/
    │   │           ├── finanzas/
    │   │           ├── recurso/
    │   │           ├── reporte/
    │   │           ├── security/
    │   │           ├── sede/
    │   │           ├── tarifa/
    │   │           ├── umbral/
    │   │           └── usuario/
    │   └── resources/
    │       ├── application.properties
    │       ├── static/
    │       │   └── css/
    │       │       └── app.css
    │       └── templates/
    │           ├── alertas/
    │           ├── auditorias/
    │           ├── auth/
    │           ├── consumos/
    │           ├── dashboard/
    │           ├── eventos-acceso/
    │           ├── monedas/
    │           ├── reportes/
    │           ├── sedes/
    │           ├── tarifas/
    │           ├── tipos-cambio/
    │           ├── tipos-recurso/
    │           ├── umbrales/
    │           └── usuarios/
    └── test/
        ├── java/
        └── resources/
```

## 7. Division Por Modulos

### 7.1 Modulo Auth

Ruta principal:

```text
src/main/java/com/luxury/auth
```

Responsabilidad:

- Mostrar formulario de login.
- Mostrar formulario de registro.
- Registrar nuevos usuarios.
- Convertir formularios web en DTOs de solicitud.

Archivos principales:

- `AuthController.java`
- `AuthService.java`
- `LoginForm.java`
- `RegisterForm.java`
- `RegisterRequest.java`

### 7.2 Modulo Security

Ruta principal:

```text
src/main/java/com/luxury/security
```

Responsabilidad:

- Configurar Spring Security.
- Definir rutas publicas y protegidas.
- Controlar acceso por roles.
- Cargar usuarios desde la base de datos.
- Obtener el usuario autenticado.

Archivos principales:

- `SecurityConfig.java`
- `UserDetailsServiceImpl.java`
- `AuthenticatedUserService.java`

### 7.3 Modulo Usuario

Ruta principal:

```text
src/main/java/com/luxury/usuario
```

Responsabilidad:

- Representar usuarios, roles y permisos.
- Manejar relaciones muchos a muchos entre usuarios y roles.
- Manejar relaciones muchos a muchos entre roles y permisos.
- Listar usuarios y ver detalle de usuario.

Tablas relacionadas:

- `usuarios`
- `roles`
- `permisos`
- `usuario_roles`
- `rol_permisos`

### 7.4 Modulo Sede

Ruta principal:

```text
src/main/java/com/luxury/sede
```

Responsabilidad:

- Registrar sedes corporativas.
- Listar sedes.
- Editar sedes.
- Desactivar sedes.

Tabla relacionada:

- `sedes`

### 7.5 Modulo Recurso

Ruta principal:

```text
src/main/java/com/luxury/recurso
```

Responsabilidad:

- Registrar tipos de recurso.
- Listar tipos de recurso.
- Eliminar tipos de recurso.

Ejemplos:

- Luz, unidad kWh.
- Agua, unidad m3.
- Gas, unidad m3.

Tabla relacionada:

- `tipos_recurso`

### 7.6 Modulo Tarifa

Ruta principal:

```text
src/main/java/com/luxury/tarifa
```

Responsabilidad:

- Registrar tarifas por sede y tipo de recurso.
- Guardar precio unitario en soles.
- Definir vigencia mediante fecha de inicio y fecha de fin.
- Buscar la tarifa vigente al registrar un consumo.

Tabla relacionada:

- `tarifas_recurso`

### 7.7 Modulo Consumo

Ruta principal:

```text
src/main/java/com/luxury/consumo
```

Responsabilidad:

- Registrar consumos operativos.
- Asociar cada consumo a una sede, recurso, tarifa y usuario.
- Calcular el costo base en PEN.
- Coordinar la conversion financiera.
- Coordinar la generacion de alertas.
- Registrar auditoria de creacion de consumo.

Tabla relacionada:

- `consumos`

### 7.8 Modulo Finanzas

Ruta principal:

```text
src/main/java/com/luxury/finanzas
```

Responsabilidad:

- Registrar monedas.
- Registrar tipos de cambio.
- Calcular costos de consumo en PEN, USD y EUR.
- Guardar el monto calculado y el tipo de cambio usado.

Tablas relacionadas:

- `monedas`
- `tipos_cambio`
- `consumo_costos`

### 7.9 Modulo Umbral

Ruta principal:

```text
src/main/java/com/luxury/umbral
```

Responsabilidad:

- Registrar limites por sede y recurso.
- Definir limite de consumo.
- Definir limite de presupuesto en soles.
- Desactivar umbrales.
- Buscar el umbral vigente al registrar un consumo.

Tabla relacionada:

- `umbrales`

### 7.10 Modulo Alerta

Ruta principal:

```text
src/main/java/com/luxury/alerta
```

Responsabilidad:

- Evaluar si un consumo supera el umbral.
- Generar alertas por exceso de consumo.
- Generar alertas por exceso de presupuesto.
- Listar alertas.
- Marcar alertas como atendidas.

Tabla relacionada:

- `alertas`

### 7.11 Modulo Auditoria

Ruta principal:

```text
src/main/java/com/luxury/auditoria
```

Responsabilidad:

- Registrar acciones importantes realizadas por usuarios.
- Consultar auditorias.
- Filtrar auditorias por usuario.
- Filtrar auditorias por modulo.

Tabla relacionada:

- `auditorias`

### 7.12 Modulo Evento Acceso

Ruta principal:

```text
src/main/java/com/luxury/eventoacceso
```

Responsabilidad:

- Registrar eventos relacionados con acceso al sistema.
- Listar eventos de acceso.
- Dejar trazabilidad de login, fallos o accesos.

Tabla relacionada:

- `eventos_acceso`

### 7.13 Modulo Dashboard

Ruta principal:

```text
src/main/java/com/luxury/dashboard
```

Responsabilidad:

- Mostrar resumen general del sistema.
- Mostrar total de sedes, consumos y alertas.
- Mostrar costos totales por moneda.
- Mostrar consumo agrupado por sede.
- Mostrar costos agrupados por mes.

Este modulo no tiene tabla propia. Lee informacion de las tablas existentes mediante consultas JPQL.

### 7.14 Modulo Reporte

Ruta principal:

```text
src/main/java/com/luxury/reporte
```

Responsabilidad:

- Generar reporte mensual por periodo.
- Generar reporte por sede.
- Agrupar consumo y costos por sede, recurso y periodo.

Este modulo no tiene tabla propia. Funciona como modulo de consulta.

### 7.15 Modulo Common

Ruta principal:

```text
src/main/java/com/luxury/common
```

Responsabilidad:

- Definir enums generales.
- Manejar excepciones comunes.
- Cargar datos iniciales.

Archivos importantes:

- `DataInitializer.java`
- `EstadoRegistro.java`
- `EstadoAlerta.java`
- `NivelAlerta.java`
- `TipoAlerta.java`
- `TipoEventoAcceso.java`
- `BusinessException.java`
- `ResourceNotFoundException.java`

## 8. Estructura Interna De Cada Modulo

La mayoria de modulos siguen esta separacion:

```text
modulo/
├── controller/
├── dto/
├── model/
├── repository/
└── service/
```

Significado:

- `controller`: recibe peticiones web y retorna vistas Thymeleaf.
- `dto`: contiene formularios, requests y responses.
- `model`: contiene entidades JPA que representan tablas.
- `repository`: contiene interfaces Spring Data JPA.
- `service`: contiene la logica de negocio.

No todos los modulos tienen todas las carpetas. Por ejemplo, `dashboard` y `reporte` no tienen entidades propias porque consultan datos ya existentes.

## 9. Base De Datos

La base de datos final tiene 16 tablas:

```text
usuarios
roles
permisos
usuario_roles
rol_permisos
sedes
tipos_recurso
tarifas_recurso
consumos
monedas
tipos_cambio
consumo_costos
umbrales
alertas
auditorias
eventos_acceso
```

### Relaciones Principales

```text
usuarios N:M roles
roles N:M permisos

sedes 1:N tarifas_recurso
tipos_recurso 1:N tarifas_recurso

sedes 1:N consumos
tipos_recurso 1:N consumos
tarifas_recurso 1:N consumos
usuarios 1:N consumos

consumos 1:N consumo_costos
monedas 1:N consumo_costos
tipos_cambio 1:N consumo_costos

sedes 1:N umbrales
tipos_recurso 1:N umbrales

consumos 1:N alertas
umbrales 1:N alertas

usuarios 1:N auditorias
usuarios 1:N eventos_acceso
```

## 10. Flujo Principal Del Sistema

El flujo mas importante es el registro de consumo:

```text
1. El usuario inicia sesion.
2. El sistema valida sus credenciales con Spring Security.
3. El usuario abre el formulario de registro de consumo.
4. El sistema carga sedes y tipos de recurso.
5. El usuario ingresa sede, recurso, cantidad, fecha y periodo.
6. ConsumoController recibe el formulario.
7. ConsumoService obtiene el usuario autenticado.
8. ConsumoService busca la sede y el recurso.
9. TarifaService busca la tarifa vigente.
10. Se guarda el consumo.
11. Se calcula el costo base en PEN.
12. ConversionFinancieraService calcula y guarda PEN, USD y EUR.
13. ReglasAlertasService revisa umbrales.
14. Si se supera un limite, se crea una alerta.
15. AuditoriaService registra la accion.
16. El sistema redirige al detalle del consumo.
```

## 11. Rutas Principales

### Autenticacion

```text
GET  /login
GET  /registro
POST /registro
POST /login
POST /logout
```

### Dashboard

```text
GET /
GET /dashboard
```

### Sedes

```text
GET  /sedes
GET  /sedes/registrar
POST /sedes
GET  /sedes/{id}/editar
POST /sedes/{id}/editar
POST /sedes/{id}/eliminar
```

### Tipos De Recurso

```text
GET  /tipos-recurso
GET  /tipos-recurso/registrar
POST /tipos-recurso
POST /tipos-recurso/{id}/eliminar
```

### Monedas

```text
GET  /monedas
POST /monedas
```

### Tipos De Cambio

```text
GET  /tipos-cambio
POST /tipos-cambio
POST /tipos-cambio/{id}/actualizar
```

### Tarifas

```text
GET  /tarifas
POST /tarifas
```

### Umbrales

```text
GET  /umbrales
POST /umbrales
POST /umbrales/{id}/eliminar
```

### Consumos

```text
GET  /consumos
GET  /consumos/{id}
GET  /consumos/sede/{idSede}
GET  /consumos/periodo/{periodo}
GET  /consumos/registrar
POST /consumos/registrar
```

### Alertas

```text
GET  /alertas
GET  /alertas/sede/{idSede}
POST /alertas/{id}/atender
```

### Auditorias

```text
GET /auditorias
GET /auditorias/usuario/{idUsuario}
GET /auditorias/modulo/{modulo}
```

### Eventos De Acceso

```text
GET /eventos-acceso
```

### Reportes

```text
GET /reportes/mensual?periodo=2026-05
GET /reportes/sede/{idSede}
```

### Usuarios

```text
GET /usuarios
GET /usuarios/{id}
```

## 12. Seguridad

El sistema usa Spring Security con autenticacion por formulario. Las credenciales se validan contra la tabla `usuarios`.

Roles iniciales:

```text
ADMIN
GERENTE
ANALISTA
AUDITOR
```

Reglas principales:

- `/login`, `/registro`, `/css/**` y `/error` son rutas publicas.
- `/usuarios/**` requiere rol `ADMIN`.
- `/auditorias/**` y `/eventos-acceso/**` requieren rol `ADMIN` o `AUDITOR`.
- `/consumos/**` requiere rol `ADMIN`, `ANALISTA` o `GERENTE`.
- `/dashboard/**` y `/reportes/**` requieren usuario autenticado con rol permitido.
- Las demas rutas requieren autenticacion.

Usuario inicial:

```text
Email: admin@luxury.com
Password: admin123
Rol: ADMIN
```

## 13. Datos Iniciales

El archivo `DataInitializer.java` carga datos base al iniciar el sistema:

- Roles.
- Permisos.
- Usuario administrador.
- Monedas PEN, USD y EUR.
- Recursos Luz, Agua y Gas.
- Sedes Lima, Piura y Trujillo.
- Tipos de cambio base.
- Tarifas iniciales.
- Umbrales iniciales.

## 14. Configuracion De Base De Datos

Archivo:

```text
src/main/resources/application.properties
```

Configuracion actual:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/luxury}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:1204}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=${DDL_AUTO:update}
spring.jpa.show-sql=${SHOW_SQL:false}
spring.jpa.open-in-view=false
```

La base de datos esperada en PostgreSQL se llama:

```text
luxury
```

## 15. Como Ejecutar El Proyecto

Desde la raiz del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

Luego abrir:

```text
http://localhost:8080/login
```

Credenciales iniciales:

```text
admin@luxury.com
admin123
```

## 16. Como Ejecutar Las Pruebas

```powershell
.\mvnw.cmd clean test
```

Las pruebas usan H2 en memoria, no PostgreSQL. Esto permite validar carga de contexto, login, rutas protegidas y registro de consumo sin depender de la base local.

## 17. Resumen Para Exponer

Luxury es una aplicacion web empresarial desarrollada con Spring Boot MVC, Thymeleaf, Spring Security, JPA/Hibernate y PostgreSQL. Su objetivo es centralizar el control de consumos operativos de sedes corporativas, calculando costos en diferentes monedas, generando alertas por umbrales, registrando auditoria y mostrando reportes ejecutivos. El sistema esta organizado como monolito modular, dividido en paquetes por responsabilidad, y trabaja con una base de datos normalizada de 16 tablas.
