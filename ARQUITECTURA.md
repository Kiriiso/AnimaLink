# VetAnimaLink — Plataforma de Gestión Veterinaria

Sistema basado en **microservicios** para la gestión integral de una clínica veterinaria: clientes, mascotas, citas, usuarios/autenticación, facturación e inventario, expuestos a través de un API Gateway y consumidos por un frontend SPA.

---

## 1. Visión General de la Arquitectura

```
                                ┌────────────────────┐
                                │      Frontend       │
                                │   (SPA - Angular/   │
                                │    React + Vite)    │
                                └──────────┬──────────┘
                                           │ HTTPS / REST + JWT
                                           ▼
                                ┌────────────────────┐
                                │     API Gateway     │
                                │  (Spring Cloud GW)  │
                                └──────────┬──────────┘
              ┌──────────┬──────────┬──────┴──────┬──────────┬──────────┐
              ▼          ▼          ▼             ▼          ▼          ▼
        ┌─────────┐┌──────────┐┌───────────┐┌──────────┐┌─────────┐┌──────────┐
        │  auth   ││  users   ││ cliente    ││  pets    ││appoint- ││ factura  │
        │ service ││ service  ││  service   ││ service  ││ment svc ││ service  │
        └─────────┘└──────────┘└───────────┘└──────────┘└─────────┘└──────────┘
                                                                            │
                                                                      ┌──────────┐
                                                                      │inventory │
                                                                      │ service  │
                                                                      └──────────┘
        Cada microservicio → su propia base de datos (MySQL) + Liquibase para versionado de esquema
```

### Principios de diseño

- **Database per service**: cada microservicio gestiona su propia base de datos MySQL, sin acceso directo entre esquemas.
- **API Gateway** como único punto de entrada público (enrutamiento, balanceo, seguridad perimetral).
- **Comunicación síncrona** vía REST/JSON entre servicios (con posibilidad de migrar a eventos/mensajería en una fase posterior).
- **Autenticación centralizada** mediante `auth_service` (JWT) validado por el Gateway y/o cada servicio.
- **Versionado de esquema** con Liquibase en cada servicio.
- **Capas limpias** (Controller → Service → Repository → Entity/DTO) replicadas en todos los microservicios para mantener coherencia.

---

## 2. Estructura General del Repositorio

```
vet-animalink/
├── api-gateway/
├── auth_service/
├── users_service/
├── cliente_service/
├── pets_service/
├── appointment_service/
├── factura_service/
├── inventory_service/
├── frontend/
├── docker-compose.yml
└── README.md
```

---

## 3. Estructura Estándar de un Microservicio (Spring Boot)

Todos los microservicios de negocio (`cliente_service`, `pets_service`, `appointment_service`, `users_service`, `factura_service`, `inventory_service`) siguen la **misma plantilla de capas**, basada en Spring Boot 3/4 + Java 21 + Maven.

```
<nombre>_service/
├── src/
│   ├── main/
│   │   ├── java/com/vet/<nombre>_service/
│   │   │   ├── <Nombre>ServiceApplication.java     # Clase principal (@SpringBootApplication)
│   │   │   │
│   │   │   ├── config/                             # Configuración general
│   │   │   │   ├── SecurityConfig.java             # Seguridad / filtros JWT
│   │   │   │   ├── CorsConfig.java                 # CORS para el frontend
│   │   │   │   └── OpenApiConfig.java              # Swagger/OpenAPI
│   │   │   │
│   │   │   ├── controller/                         # Capa REST (entrada HTTP)
│   │   │   │   └── <Entidad>Controller.java
│   │   │   │
│   │   │   ├── service/                            # Lógica de negocio
│   │   │   │   ├── <Entidad>Service.java           # Interfaz
│   │   │   │   └── impl/<Entidad>ServiceImpl.java  # Implementación
│   │   │   │
│   │   │   ├── repository/                         # Acceso a datos (Spring Data JPA)
│   │   │   │   └── <Entidad>Repository.java
│   │   │   │
│   │   │   ├── model/ (entity/)                    # Entidades JPA
│   │   │   │   └── <Entidad>.java
│   │   │   │
│   │   │   ├── dto/                                # Objetos de transferencia
│   │   │   │   ├── request/<Entidad>RequestDTO.java
│   │   │   │   └── response/<Entidad>ResponseDTO.java
│   │   │   │
│   │   │   ├── mapper/                             # Mapeo Entity <-> DTO (MapStruct)
│   │   │   │   └── <Entidad>Mapper.java
│   │   │   │
│   │   │   ├── exception/                          # Manejo centralizado de errores
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   └── GlobalExceptionHandler.java     # @RestControllerAdvice
│   │   │   │
│   │   │   └── client/                             # Clientes REST hacia otros microservicios
│   │   │       └── <OtroServicio>Client.java       # Feign Client o RestClient
│   │   │
│   │   └── resources/
│   │       ├── application.properties              # Configuración (perfil default)
│   │       ├── application-dev.properties          # Perfil desarrollo
│   │       ├── application-docker.properties        # Perfil contenedores
│   │       └── db/changelog/                       # Liquibase
│   │           ├── db.changelog-master.yaml
│   │           └── changes/
│   │               └── 001-create-<tabla>-table.yaml
│   │
│   └── test/
│       └── java/com/vet/<nombre>_service/
│           ├── <Nombre>ServiceApplicationTests.java
│           ├── controller/<Entidad>ControllerTest.java
│           └── service/<Entidad>ServiceTest.java
│
├── Dockerfile
├── pom.xml
├── mvnw / mvnw.cmd
└── .gitignore
```

### Dependencias base (pom.xml) por microservicio

- `spring-boot-starter-web` (o `webmvc` según versión)
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-liquibase`
- `spring-boot-starter-validation`
- `spring-boot-starter-security` (en servicios que validan JWT)
- `mysql-connector-j`
- `lombok`
- `mapstruct` (mapeo DTO)
- `springdoc-openapi-starter-webmvc-ui` (documentación Swagger)
- Dependencias de test: `spring-boot-starter-test`, `spring-boot-starter-data-jpa-test`, `spring-boot-starter-liquibase-test`

---

## 4. Servicios del Sistema

### 4.1 `api-gateway`
- **Responsabilidad**: punto único de entrada, enrutamiento a microservicios, filtros de seguridad (validación JWT), CORS global, rate limiting.
- **Tecnología**: Spring Cloud Gateway.
- **Rutas típicas**:
  - `/api/auth/**` → `auth_service`
  - `/api/users/**` → `users_service`
  - `/api/clientes/**` → `cliente_service`
  - `/api/pets/**` → `pets_service`
  - `/api/appointments/**` → `appointment_service`
  - `/api/facturas/**` → `factura_service`
  - `/api/inventory/**` → `inventory_service`

### 4.2 `auth_service`
- **Responsabilidad**: registro/login, generación y validación de JWT, roles y permisos (ADMIN, VETERINARIO, RECEPCIONISTA, CLIENTE).
- **Entidades**: `Usuario`, `Rol`.
- **Endpoints clave**: `POST /auth/register`, `POST /auth/login`, `POST /auth/refresh`, `GET /auth/validate`.

### 4.3 `users_service`
- **Responsabilidad**: gestión de perfiles de usuario internos (veterinarios, personal administrativo) y sus datos.
- **Entidades**: `Usuario`, `Perfil`.

### 4.4 `cliente_service`
- **Responsabilidad**: gestión de clientes (dueños de mascotas) — datos personales, contacto, dirección.
- **Entidad principal**: `Cliente { id, nombre, apellido, email, telefono, direccion, dni, fechaRegistro }`.
- **Relaciones**: un cliente puede tener N mascotas (referenciadas vía `pets_service` por `clienteId`).
- **Endpoints**: `GET/POST/PUT/DELETE /clientes`, `GET /clientes/{id}/mascotas` (orquestado vía cliente HTTP a `pets_service`).

### 4.5 `pets_service`
- **Responsabilidad**: gestión de mascotas — datos clínicos básicos, especie, raza, historial asociado.
- **Entidad principal**: `Mascota { id, nombre, especie, raza, fechaNacimiento, peso, clienteId, estado }`.
- **Relación**: `clienteId` referencia al `cliente_service` (sin FK física, comunicación vía API).
- **Endpoints**: `GET/POST/PUT/DELETE /pets`, `GET /pets/cliente/{clienteId}`.

### 4.6 `appointment_service`
- **Responsabilidad**: gestión de citas (turnos) — agenda, estado, asignación a veterinario.
- **Entidad principal**: `Cita { id, mascotaId, clienteId, veterinarioId, fechaHora, motivo, estado }`.
- **Estados de cita**: `PENDIENTE`, `CONFIRMADA`, `EN_CURSO`, `COMPLETADA`, `CANCELADA`.
- **Comunicación**: valida existencia de `mascotaId` (→ `pets_service`) y `clienteId` (→ `cliente_service`) antes de crear la cita.
- **Endpoints**: `GET/POST/PUT/DELETE /appointments`, `GET /appointments/cliente/{clienteId}`, `GET /appointments/mascota/{mascotaId}`, `PATCH /appointments/{id}/estado`.

### 4.7 `factura_service`
- **Responsabilidad**: generación de facturas asociadas a citas/servicios/productos consumidos.
- **Entidad principal**: `Factura { id, clienteId, citaId, items[], total, fechaEmision, estado }`.
- **Comunicación**: consulta `cliente_service` (datos del cliente), `appointment_service` (servicios prestados) e `inventory_service` (productos usados).

### 4.8 `inventory_service`
- **Responsabilidad**: control de stock de medicamentos, productos e insumos veterinarios.
- **Entidad principal**: `Producto { id, nombre, categoria, stock, precioUnitario, proveedor }`.
- **Endpoints**: `GET/POST/PUT/DELETE /inventory`, `PATCH /inventory/{id}/stock`.

---

## 5. Comunicación entre Microservicios

- **Síncrona REST** mediante `RestClient` / `WebClient` o **Feign Clients** declarados en la capa `client/` de cada servicio.
- Cada cliente HTTP define explícitamente el contrato (DTO) que espera del otro servicio, evitando dependencias de código compartido.
- El **API Gateway** resuelve el descubrimiento de servicios (vía configuración estática o **Eureka/Consul** si se incorpora Service Discovery).
- Manejo de errores entre servicios mediante `GlobalExceptionHandler` + códigos HTTP estándar (404, 409, 422, etc.).

```
appointment_service
   ├── client/PetsServiceClient.java     → GET /pets/{id}
   └── client/ClienteServiceClient.java  → GET /clientes/{id}
```

---

## 6. Base de Datos y Migraciones

- Motor: **MySQL** (`mysql-connector-j`).
- Cada servicio posee su propio esquema: `vet_cliente_db`, `vet_pets_db`, `vet_appointment_db`, etc.
- **Liquibase** gestiona el versionado:
  ```
  src/main/resources/db/changelog/
  ├── db.changelog-master.yaml
  └── changes/
      ├── 001-create-<tabla>-table.yaml
      ├── 002-add-<columna>-to-<tabla>.yaml
      └── ...
  ```
- Convención de nombres: `XXX-<accion>-<entidad>.yaml` con numeración incremental.

---

## 7. Seguridad

- `auth_service` emite tokens **JWT** (access + refresh token).
- El **API Gateway** valida la firma del JWT en un filtro global antes de enrutar.
- Cada microservicio puede revalidar el token y extraer roles/claims para autorización a nivel de endpoint (`@PreAuthorize`).
- Roles sugeridos: `ROLE_ADMIN`, `ROLE_VETERINARIO`, `ROLE_RECEPCIONISTA`, `ROLE_CLIENTE`.

---

## 8. Frontend (SPA)

```
frontend/
├── src/
│   ├── app/ (o src/)
│   │   ├── core/
│   │   │   ├── auth/            # Login, guards, interceptores JWT
│   │   │   ├── services/        # Servicios HTTP por dominio (cliente.service, pets.service, etc.)
│   │   │   └── models/          # Interfaces/DTOs TypeScript
│   │   ├── shared/               # Componentes reutilizables (botones, tablas, modales)
│   │   ├── features/
│   │   │   ├── clientes/
│   │   │   ├── mascotas/
│   │   │   ├── citas/
│   │   │   ├── facturacion/
│   │   │   └── inventario/
│   │   └── layout/                # Navbar, sidebar, layout base
│   ├── environments/
│   │   ├── environment.ts
│   │   └── environment.prod.ts
│   └── main.ts
├── package.json
└── vite.config.ts (o angular.json)
```

- Toda petición pasa por el **API Gateway** (`environment.apiUrl = http://localhost:8080/api`).
- Interceptor HTTP añade el JWT en cabecera `Authorization: Bearer <token>`.
- Cada `feature` (clientes, mascotas, citas...) sigue patrón: `pages/ + components/ + services/ + models/`.

---

## 9. Contenerización y Orquestación

```
vet-animalink/
├── docker-compose.yml
├── api-gateway/Dockerfile
├── auth_service/Dockerfile
├── cliente_service/Dockerfile
├── pets_service/Dockerfile
├── appointment_service/Dockerfile
├── factura_service/Dockerfile
├── inventory_service/Dockerfile
├── users_service/Dockerfile
└── frontend/Dockerfile
```

- `docker-compose.yml` define: una instancia de MySQL por servicio (o un MySQL multi-base), cada microservicio, el API Gateway y el frontend.
- Perfiles `application-docker.properties` apuntan a los hostnames de los contenedores (`db-cliente`, `db-pets`, etc.).

---

## 10. Puertos sugeridos (entorno local)

| Servicio              | Puerto |
|-----------------------|--------|
| api-gateway           | 8080   |
| auth_service          | 8081   |
| users_service         | 8082   |
| cliente_service       | 8083   |
| pets_service          | 8084   |
| appointment_service   | 8085   |
| factura_service       | 8086   |
| inventory_service     | 8087   |
| frontend              | 4200 / 5173 |

---

## 11. Buenas Prácticas Aplicadas

- Separación estricta de capas (Controller / Service / Repository / DTO / Mapper).
- DTOs de entrada y salida diferenciados (nunca expongo entidades JPA directamente).
- Manejo global de excepciones con respuestas de error estandarizadas (`ApiError { timestamp, status, message, path }`).
- Validaciones con `jakarta.validation` (`@NotNull`, `@Email`, etc.) en los DTOs de request.
- Documentación automática con OpenAPI/Swagger en cada servicio (`/swagger-ui.html`).
- Tests unitarios e de integración por capa (`service` y `controller`).
- Convenciones de commits y ramas (`feature/`, `fix/`, `release/`) para trabajo colaborativo.

---

## 12. Próximos Pasos Sugeridos

1. Completar la capa de dominio (entidades, DTOs, controladores) en cada microservicio siguiendo la plantilla de la sección 3.
2. Implementar `auth_service` con JWT y proteger el `api-gateway`.
3. Configurar Liquibase con el primer changelog por servicio.
4. Definir los clientes REST entre `appointment_service` ↔ `pets_service` / `cliente_service`.
5. Levantar `docker-compose.yml` con MySQL + servicios + frontend.
6. Documentar cada API con Swagger y centralizar el contrato en una colección Postman/Bruno.
