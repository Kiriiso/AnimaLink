# AnimaLink — Requisitos, Descargas y Guía de Ejecución

Sistema veterinario en **arquitectura de microservicios** (Spring Boot 4.1.0 + Java 21).
Este documento explica **qué instalar, de dónde descargarlo y cómo levantar el proyecto** desde cero.

> ⚠️ **Importante sobre tu equipo actual:** hoy tienes instalado solo **Java 17 (JRE/JDK 17)**, pero
> este proyecto está configurado para **Java 21**. Debes instalar **JDK 21** (paso 1) o el proyecto **no compilará**.

---

## 1. Software obligatorio (qué descargar y dónde)

| # | Software | Versión | Para qué sirve | Dónde descargarlo |
|---|----------|---------|----------------|-------------------|
| 1 | **JDK 21** (Eclipse Temurin) | 21 LTS | Compilar y ejecutar los microservicios | https://adoptium.net/es/temurin/releases/?version=21 |
| 2 | **MySQL** (vía XAMPP) | 8.x | Base de datos de cada microservicio | https://www.apachefriends.org/es/index.html |
| 3 | **Visual Studio Code** | Última | Editor / IDE | https://code.visualstudio.com/ |
| 4 | **Git** | Última | Control de versiones (GitHub) | https://git-scm.com/downloads |
| 5 | **Postman** *(o Thunder Client en VS Code)* | Última | Probar las APIs REST | https://www.postman.com/downloads/ |

> **Maven** NO necesitas instalarlo aparte: cada microservicio trae el **Maven Wrapper** (`mvnw.cmd`),
> que descarga Maven automáticamente. (Si igual lo quieres global: https://maven.apache.org/download.cgi)

### Alternativas válidas (según el Word de la asignatura)
- Base de datos: en vez de XAMPP puedes usar **Laragon** (https://laragon.org/download/) o **MySQL Community + Workbench** (https://dev.mysql.com/downloads/installer/). El motor también puede ser **Oracle**.
- IDE: en vez de VS Code puedes usar **IntelliJ IDEA Community** (https://www.jetbrains.com/idea/download/).

---

## 2. Extensiones recomendadas en VS Code

Instálalas desde la pestaña *Extensions* (Ctrl+Shift+X):

1. **Extension Pack for Java** (Microsoft) — incluye compilador, debugger y Maven.
2. **Spring Boot Extension Pack** (VMware) — soporte para Spring Boot.
3. **Thunder Client** (opcional) — probar APIs REST sin salir del editor.

---

## 3. Configurar el JDK 21 (después de instalarlo)

1. Instala el JDK 21 desde Adoptium (paso 1). Marca la opción **"Set JAVA_HOME variable"** durante la instalación.
2. Cierra y reabre la terminal. Verifica:
   ```bash
   java -version      # debe decir 21.x
   javac -version     # debe decir 21.x
   echo %JAVA_HOME%   # debe apuntar a ...\jdk-21...
   ```
3. Si `java -version` sigue mostrando 17, edita la variable de entorno `JAVA_HOME` a la carpeta del JDK 21
   (ej. `C:\Program Files\Eclipse Adoptium\jdk-21.0.x-hotspot`) y agrega `%JAVA_HOME%\bin` al `Path`.

---

## 4. Crear las bases de datos (una por microservicio)

El Word exige que **cada microservicio tenga su propia base de datos**. Con XAMPP:

1. Abre el **XAMPP Control Panel** → inicia **Apache** y **MySQL**.
2. Entra a **phpMyAdmin**: http://localhost/phpmyadmin
3. Ejecuta este SQL (pestaña *SQL*) para crear las 9 bases:

```sql
CREATE DATABASE IF NOT EXISTS animalink_auth;
CREATE DATABASE IF NOT EXISTS animalink_usuario;
CREATE DATABASE IF NOT EXISTS animalink_cliente;
CREATE DATABASE IF NOT EXISTS animalink_mascota;
CREATE DATABASE IF NOT EXISTS animalink_cita;
CREATE DATABASE IF NOT EXISTS animalink_historial;
CREATE DATABASE IF NOT EXISTS animalink_control_alta;
CREATE DATABASE IF NOT EXISTS animalink_inventario;
CREATE DATABASE IF NOT EXISTS animalink_factura;
```

> Con `spring.jpa.hibernate.ddl-auto=update`, las **tablas se crean solas** al iniciar cada servicio.
> Solo necesitas crear las bases (los `CREATE DATABASE` de arriba).

### Usuario/contraseña de MySQL
- XAMPP por defecto: usuario `root` **sin contraseña**.
- Cada servicio usa el perfil `dev` (`application-dev.properties`). Si tu MySQL tiene contraseña, ajústala ahí.

---

## 5. Cómo ejecutar los microservicios

Cada servicio se levanta **por separado** (son independientes). Abre una terminal **por servicio**.

```bash
# Ejemplo: levantar cliente_service
cd kiri/AnimaLink-main/cliente_service
./mvnw.cmd spring-boot:run        # En PowerShell:  .\mvnw.cmd spring-boot:run
```

### Orden de arranque recomendado
Como hay servicios que se consultan entre sí (vía WebClient), conviene este orden:

1. `auth_service`        (8081)
2. `usuario_service`     (8082)
3. `cliente_service`     (8083)
4. `inventario_service`  (8088)
5. `mascota_service`     (8084)  → consulta a cliente
6. `cita_service`        (8085)  → consulta a mascota y usuario
7. `historial_service`   (8086)  → consulta a mascota
8. `control_alta_service`(8087)  → consulta a mascota
9. `factura_service`     (8089)  → consulta a cliente e inventario
10. `gate-way`           (8080)  → enruta a todos

> El sistema **igual funciona** si un servicio dependiente no está arriba: las validaciones por WebClient
> simplemente devolverán un error controlado (404/400) en lugar de tumbar el servicio.

---

## 6. Puertos, bases de datos y URLs

| Microservicio | Puerto | Base de datos | Swagger UI |
|---------------|--------|---------------|------------|
| gate-way | 8080 | — | — |
| auth_service | 8081 | animalink_auth | http://localhost:8081/doc/swagger-ui.html |
| usuario_service | 8082 | animalink_usuario | http://localhost:8082/doc/swagger-ui.html |
| cliente_service | 8083 | animalink_cliente | http://localhost:8083/doc/swagger-ui.html |
| mascota_service | 8084 | animalink_mascota | http://localhost:8084/doc/swagger-ui.html |
| cita_service | 8085 | animalink_cita | http://localhost:8085/doc/swagger-ui.html |
| historial_service | 8086 | animalink_historial | http://localhost:8086/doc/swagger-ui.html |
| control_alta_service | 8087 | animalink_control_alta | http://localhost:8087/doc/swagger-ui.html |
| inventario_service | 8088 | animalink_inventario | http://localhost:8088/doc/swagger-ui.html |
| factura_service | 8089 | animalink_factura | http://localhost:8089/doc/swagger-ui.html |

A través del **gateway** (puerto 8080) puedes consumir todo con el prefijo del servicio, por ejemplo:
`http://localhost:8080/api/v1/clientes`, `http://localhost:8080/api/v1/mascotas`, etc.

---

## 7. Probar el sistema (flujo mínimo de demostración)

1. **Registrar/iniciar sesión** en `auth_service`:
   `POST http://localhost:8081/api/v1/auth/register`  → devuelve un **token JWT**.
2. **Crear un cliente** en `cliente_service`:
   `POST http://localhost:8083/api/v1/clientes`.
3. **Crear una mascota** asociada a ese cliente en `mascota_service`:
   `POST http://localhost:8084/api/v1/mascotas` (valida que el cliente exista vía WebClient).
4. **Agendar una cita** en `cita_service` (valida mascota + veterinario).
5. **Registrar historial / control de alta / factura**.

> Al iniciar, cada servicio carga **datos de prueba automáticos con DataFaker**, así que ya habrá
> clientes, mascotas, etc. de ejemplo para probar de inmediato.

---

## 8. Resumen de requisitos del proyecto (rúbrica) y dónde se cumplen

| Requisito del Word | Cómo se cumple en AnimaLink |
|--------------------|------------------------------|
| Mínimo 10 microservicios Spring Boot | 9 servicios de negocio + API Gateway (ampliable) |
| Responsabilidad clara + API REST + endpoints | Cada servicio tiene su dominio, controller y endpoints REST |
| Desacoplados | Cada uno es un proyecto Maven independiente con su propia BD |
| Comunicación entre servicios (WebClient) | mascota→cliente, cita→mascota/usuario, factura→cliente/inventario, etc. |
| API Gateway | `gate-way` con Spring Cloud Gateway |
| BD independiente por servicio | 9 esquemas MySQL distintos (`animalink_*`) |
| 2–3 roles | `ADMIN`, `VETERINARIO`, `RECEPCIONISTA` (auth_service) |
| Seguridad (hash, login, token) | BCrypt + JWT en auth_service |
| CRUD + operaciones personalizadas + validaciones | Controllers V1 (CRUD) y V2 (filtros) + Bean Validation |
| Pruebas unitarias | Tests de service y controller por microservicio |
| Documentación | Swagger/OpenAPI (springdoc) en cada servicio |

---

## 9. Errores típicos y solución

| Problema | Causa | Solución |
|----------|-------|----------|
| `release version 21 not supported` | Estás compilando con JDK 17 | Instala **JDK 21** (paso 1) y apunta `JAVA_HOME` a él |
| `Communications link failure` | MySQL apagado | Inicia MySQL en XAMPP |
| `Unknown database 'animalink_x'` | No creaste las BD | Ejecuta el SQL del paso 4 |
| `Port 808x already in use` | Otro proceso usa el puerto | Cierra el proceso o cambia el puerto en `application.properties` |
| Swagger no abre | Ruta equivocada | Usa `/doc/swagger-ui.html` (no `/swagger-ui.html`) |
