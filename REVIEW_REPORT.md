# Informe de Revisión de Microservicios - AnimaLink

Fecha: 2026-06-18

## Resumen General

Este informe recoge los hallazgos principales detectados en los microservicios del proyecto AnimaLink. Se revisaron los siguientes elementos clave en cada servicio:

- `pom.xml`
- `Dockerfile`
- `docker-compose.yml` (raíz)
- Controladores (`Controller`)
- Entidades / Modelos
- DTOs
- Manejo de excepciones
- Servicios
- Repositorios
- Configuración (`Config`, `application.properties`)
- Clientes HTTP

En general, se identificaron las siguientes categorías de fallos:

- Dependencias repetidas e inconsistentes entre servicios.
- Falta de validación explícita en DTOs.
- Exposición innecesaria de campos sensibles en respuestas.
- Configuración de seguridad inconsistente o ausente.
- Uso de `spring-boot-starter-security` sin una configuración de seguridad personalizada, que puede provocar comportamientos inesperados de autenticación básica por defecto.
- Configuración de gateway con rutas duplicadas y llamadas bloqueantes en WebClient.
- Dockerfiles correctos en cuanto a multi-stage build, pero sin healthchecks y con posible mejora de cache.
- `docker-compose.yml` con dependencias de servicio correctas, aunque sin healthchecks y con nombre de servicio `auth_service` usado como registro de Eureka en modo local.

---

## Hallazgos por Microservicio

### 1. auth_service

#### Problemas detectados
- **`auth_service/auth_service/pom.xml`**
  - Se declara `spring-cloud-starter-netflix-eureka-server` en un servicio que no actúa como servidor Eureka.
  #(Resolucion Kirii)Declaracion eliminada
  - Se declara `spring-boot-starter-security` sin definir configuración de seguridad; esto puede activar seguridad básica por defecto en Spring Boot.
  #(Resolucion Kirii)Declaracion se mantiene por posible utilidad
  - Todas las dependencias usan Spring Boot `3.2.4`, lo cual es consistente, pero no hay un `root pom.xml` de parent para el conjunto del proyecto.

- **`auth_service/auth_service/src/main/java/com/vet/auth_service/dto/AuthUserResponseDTO.java`**
  - Contiene el campo `password`, lo que expone información sensible en respuestas API.

- **`auth_service/auth_service/src/main/java/com/vet/auth_service/dto/AuthUserRequestDTO.java`**
  - Faltan anotaciones de validación (`@NotBlank`, `@Email`, `@Size`, etc.).

- **`auth_service/auth_service/src/main/java/com/vet/auth_service/model/AuthUser.java`**
  - No hay validaciones JPA ni `@Column` para definir longitudes / restricciones.

- **`auth_service/auth_service/src/main/java/com/vet/auth_service/service/impl/AuthUserServiceImpl.java`**
  - La contraseña se guarda sin encriptar.
  - El servicio se expone usando la implementación `AuthUserServiceImpl` en el controlador en lugar de la interfaz `AuthUserService`.

- **`auth_service/auth_service/src/main/java/com/vet/auth_service/controller/AuthUserController.java`**
  - Aunque usa `@Valid`, el DTO no define restricciones de validación.

- **`auth_service/auth_service/Dockerfile`**
  - El Dockerfile es aceptable, pero no define `HEALTHCHECK`.
  - Falta optimización de caching adicional: podría copiar `pom.xml` y resolver dependencias antes de copiar todo el código.

- **`auth_service/auth_service/src/main/resources/application.properties`**
  - Usa `spring.jpa.hibernate.ddl-auto=update` en producción potencial.
  - Usa credenciales frías (`root/root`) hardcodeadas.
  - Configura Eureka como servidor en el propio servicio (`eureka.server.enable-self-preservation=false`), lo cual es inconsistente con el rol del servicio.

#### Recomendaciones

- Quitar `spring-cloud-starter-netflix-eureka-server` si el servicio no es servidor Eureka.
- Eliminar `password` de la respuesta DTO.
- Añadir validaciones en `AuthUserRequestDTO` y en la entidad.
- Añadir hashing de contraseñas con `PasswordEncoder`.
- Definir un `SecurityConfig` o deshabilitar `spring-boot-starter-security` si no se requiere.
- Añadir `HEALTHCHECK` al Dockerfile.
- Evitar `spring.jpa.hibernate.ddl-auto=update` en ambientes productivos.

---

### 2. api-gateway

#### Problemas detectados

- **`api-gateway/api-gateway/pom.xml`**
  - Declara `springdoc-openapi-starter-webflux-ui` en un proyecto con WebFlux y gateway, lo cual es apropiado, pero también declara `spring-boot-starter-validation` y `spring-boot-starter-security` sin un uso consistente.
  - No existe una dependencia de `spring-cloud-starter-gateway` en la sección `dependencyManagement`, pero sí en `dependencies` (esto está bien, solo revisar orden).

- **`api-gateway/api-gateway/src/main/resources/application.properties`**
  - Define rutas explícitas duplicadas para `/clientes/**` (`cliente-service` y `cliente-service-alias`). Esto puede causar ambigüedad.
  - Usa `spring.cloud.gateway.discovery.locator.enabled=true` junto a rutas explícitas, mezclando dos modos de definición de ruta.
  - La ruta `auth-service` mapea `/auth/**`, pero el servicio `auth_service` expone `/authusers`; hay un desajuste potencial.
  - Define propiedades de datos (`spring.datasource.*` y JPA) aunque el gateway no parece usar base de datos.

- **`api-gateway/api-gateway/src/main/java/com/vet_animalink/api_gateway/controller/GatewayController.java`**
  - Usa `WebClient` de forma bloqueante con `.block()` en controladores. Esto anula el beneficio reactivo de WebFlux y debe evitarse.
  - Construye rutas hardcodeadas `lb://cliente_service/clientes/{id}` sin fallback ni manejo de errores.
  - No hay manejo de fallos de red o timeout en las llamadas externas.

- **`api-gateway/api-gateway/src/main/java/com/vet_animalink/api_gateway/config/SecurityConfig.java`**
  - Configura `httpBasic()` y luego permite todas las solicitudes con `anyRequest().permitAll()`, lo que deja la seguridad inefectiva.
  - Deshabilita CSRF sin justificarlo.

- **`api-gateway/api-gateway/Dockerfile`**
  - No define `HEALTHCHECK`.
  - El contenedor expone el puerto correcto `8080`.

#### Recomendaciones

- Eliminar rutas duplicadas y revisar el path correcto de `auth_service`.
- Eliminar propiedades de base de datos si no son necesarias.
- Convertir el controlador a un método no bloqueante o dejar que Spring Cloud Gateway maneje el enrutamiento.
- Añadir manejo de errores/reintentos para llamadas con `WebClient` y configurar timeout.
- Ajustar el `SecurityConfig` para permitir solo lo necesario.
- Añadir `HEALTHCHECK` en Dockerfile.

---

### 3. users_service

#### Problemas detectados

- **`users_service/users_service/pom.xml`**
  - La configuración de dependencias es consistente con Spring Boot `3.2.4`.
  - No hay evidencia de dependencias faltantes, pero el servicio incluye `spring-boot-starter-security` sin un `SecurityConfig` explícito.

- **`users_service/users_service/src/main/java/com/vet/users_service/dto/UserRequestDTO.java`**
  - No existen restricciones de validación (`@NotBlank`, `@Email`, `@Size`).
  - Incluye `id` y `createdAt` en la solicitud, lo cual es una mala práctica para POST de creación.

- **`users_service/users_service/src/main/java/com/vet/users_service/model/User.java`**
  - No hay restricciones de longitud de columna ni `@Column`.
  - El campo `createdAt` no se gestiona automáticamente.

- **`users_service/users_service/src/main/java/com/vet/users_service/service/impl/UserServiceImpl.java`**
  - Actualiza `createdAt` desde el request, lo cual puede derivar en inconsistencias de auditoría.

- **`users_service/users_service/src/main/java/com/vet/users_service/controller/UserController.java`**
  - Controlador correcto, pero depende directamente de la implementación `UserServiceImpl`.

- **`users_service/users_service/src/main/resources/application.properties`**
  - Usa `spring.jpa.hibernate.ddl-auto=update` y credenciales embebidas.

#### Recomendaciones

- Añadir validaciones de entrada en `UserRequestDTO`.
- Usar `@Column(length=...)` y `@CreationTimestamp` o lógica similar para `createdAt`.
- No aceptar `id` en DTO de creación.
- Añadir configuración de seguridad o remover el starter si no se usa.

---

### 4. cliente_service

#### Problemas detectados

- **`cliente_service/cliente_service/pom.xml`**
  - Mismo patrón observado: Spring Boot `3.2.4`, dependencias consistentes.
  - `spring-boot-starter-security` presente sin un config explícito.

- **`cliente_service/cliente_service/src/main/java/com/vet/cliente_service/dto/ClienteRequestDTO.java`**
  - Falta anotaciones de validación en todos los campos.
  - Incluye `id` en la request DTO.

- **`cliente_service/cliente_service/src/main/resources/application.properties`**
  - Misma configuración de `ddl-auto=update` y credenciales embebidas.

#### Recomendaciones

- Definir validaciones en el DTO.
- Evitar recibir `id` en la solicitud de creación.
- Agregar configuración de seguridad explícita o remover el starter.

---

### 5. pets_service

#### Problemas detectados

- **`pets_service/pets_service/pom.xml`**
  - Patrón similar: `spring-boot-starter-security` sin seguridad configurada.

- **`pets_service/pets_service/src/main/java/com/vet/pets_service/dto/MascotaRequestDTO.java`**
  - Falta validación de campos.
  - Incluye `id` en la request DTO.

- **`pets_service/pets_service/src/main/resources/application.properties`**
  - Configuración de base de datos y `ddl-auto=update` igual a los demás.

#### Recomendaciones

- Añadir validación explícita en DTO.
- No exponer `id` en DTO de creación.
- Revisar seguridad activa y roles.

---

### 6. appointment_service

#### Problemas detectados

- **`appointment_service/appointment_service/pom.xml`**
  - Misma configuración de dependencias Spring Boot 3.2.4.
  - Usa `spring-boot-starter-security` sin configuración.

- **`appointment_service/appointment_service/src/main/java/com/vet/appointment_service/controller/AppointmentController.java`**
  - Correcto en forma, pero depende de `AppointmentServiceImpl`.

- **`appointment_service/appointment_service/src/main/java/com/vet/appointment_service/model/Appointment.java`**
  - No usa relaciones JPA para `clienteId` ni `mascotaId`; son simples `Long`, lo cual puede ser válido pero limita integridad referencial.

- **`appointment_service/appointment_service/src/main/java/com/vet/appointment_service/dto/AppointmentRequestDTO.java`**
  - No hay validaciones de campos.
  - Incluye `id` en request DTO.

- **`appointment_service/appointment_service/src/main/java/com/vet/appointment_service/config/WebClientConfig.java`**
  - Define un bean de `WebClient` sin timeout explícito.

- **`appointment_service/appointment_service/src/main/java/com/vet/appointment_service/config/SecurityConfig.java`**
  - Permite todos los accesos aunque habilita `httpBasic()`, por lo que la configuración no protege realmente nada.

- **Implementación de `AppointmentServiceImpl`**
  - No se encontró el archivo `AppointmentServiceImpl.java` en el árbol fuente, aunque el controlador lo importa. Esto es un error de compilación o de ubicación de clase.

#### Recomendaciones

- Añadir validaciones de DTO y no incluir `id` en creación.
- Corregir la ubicación/implementación de `AppointmentServiceImpl`.
- Revisar el WebClient para activar timeouts y manejo de errores.
- Ajustar seguridad para que sea coherente con el acceso real.

---

### 7. factura_service

#### Problemas detectados

- **`factura_service/factura_service/pom.xml`**
  - Patrón consistente con los demás servicios.

- **`factura_service/factura_service/src/main/java/com/vet/factura_service/dto/FacturaRequestDTO.java`**
  - No hay validaciones explícitas.
  - Contiene `id` en el request DTO.

- **`factura_service/factura_service/src/main/java/com/vet/factura_service/model/Factura.java`**
  - No hay columnas con restricciones definidas.

- **`factura_service/factura_service/src/main/resources/application.properties`**
  - Misma configuración general de base de datos y `ddl-auto=update`.

#### Recomendaciones

- Añadir validaciones y quitar `id` del request DTO.
- Revisar la modelación de `amount` y posibles constraints.
- Definir seguridad o remover el starter.

---

### 8. inventory_service

#### Problemas detectados

- **`inventory_service/inventory_service/pom.xml`**
  - Coincide con la plantilla de los servicios.

- **`inventory_service/inventory_service/src/main/java/com/vet/inventory_service/dto/InventoryItemRequestDTO.java`**
  - Faltan validaciones de campos.
  - `id` incluido en request DTO.

- **`inventory_service/inventory_service/src/main/resources/application.properties`**
  - Configuración estándar de base de datos sin ajustes por ambiente.

#### Recomendaciones

- Añadir validación y separar claramente request/response.
- Evitar `id` en DTO de creación.

---

### 9. medical_record_service

#### Problemas detectados

- **`medical_record_service/medical_record_service/pom.xml`**
  - Mismo patrón general.

- **`medical_record_service/medical_record_service/src/main/java/com/vet/medical_record_service/dto/MedicalRecordRequestDTO.java`**
  - No existe validación de entrada.
  - `id` en request DTO.

- **`medical_record_service/medical_record_service/src/main/resources/application.properties`**
  - Configuración de base de datos igual a otros servicios.

#### Recomendaciones

- Agregar validación a los campos definidos.
- Corregir DTO para creación.
- Revisar si debe haber relación JPA con mascotas.

---

### 10. notifications_service

#### Problemas detectados

- **`notifications_service/notifications_service/pom.xml`**
  - Patrón consistente con los otros servicios.

- **`notifications_service/notifications_service/src/main/java/com/vet/notifications_service/dto/NotificationRequestDTO.java`**
  - Falta validación explícita.
  - `id` incluido en request DTO.

- **`notifications_service/notifications_service/src/main/resources/application.properties`**
  - Misma configuración repetida con credenciales embebidas.

#### Recomendaciones

- Agregar validaciones de request.
- Separar request/response con campos necesarios.
- Corregir o eliminar seguridad no usada.

---

## Observaciones de Docker y Orquestación

- **`docker-compose.yml`**
  - Usa MySQL 8.0: apropiado, aunque no hay healthchecks definidos para servicios ni para la base de datos.
  - Los servicios se exponen con los puertos correctos de acuerdo a sus `application.properties`.
  - El servicio `api-gateway` y los microservicios dependen `auth_service` pero no existe un servicio Eureka independiente.
  - No hay variables de entorno para credenciales o URLs externas; todo está hardcodeado en `application.properties`.

- **Dockerfiles de microservicios**
  - Todos usan multi-stage build con Maven y JRE Alpine, lo cual es positivo.
  - Faltan healthchecks.
  - No se configura `JAVA_OPTS` ni timeouts explícitos.

---

## Prioridad de Correcciones

1. Corregir la seguridad global del proyecto: eliminar o configurar `spring-boot-starter-security` en todos los servicios.
2. Corregir `api-gateway` para evitar rutas duplicadas y llamadas bloqueantes con WebClient.
3. Quitar `password` de la respuesta DTO en `auth_service` y aplicar hashing de contraseñas.
4. Añadir validación explícita en todos los DTOs `RequestDTO`.
5. Verificar `AppointmentServiceImpl` y su ubicación / existencia.
6. Añadir `HEALTHCHECK` en Dockerfiles y en `docker-compose.yml`.
7. Evitar `spring.jpa.hibernate.ddl-auto=update` en producciones.
8. Centralizar configuración de servicio en variables de entorno en lugar de archivos embebidos.

---

## Conclusión

El proyecto presenta una base coherente con microservicios Spring Boot modernos, pero hay varios problemas repetidos de seguridad, validación y configuración. La mayor urgencia está en la seguridad y en la consistencia de los DTOs/servicios para evitar exposición de datos y errores de compilación.

Se recomienda una segunda iteración para implementar las correcciones priorizadas, seguida de pruebas de integración y de arranque de los servicios en Docker Compose.


#Anotaciones extra (Kirii)
Agrandar el exception para que maneje cada excepcion por separado
(Recomendacion del profesor)