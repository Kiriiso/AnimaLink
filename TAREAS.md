# TAREAS pendientes (las que debes hacer TÚ)

El código del sistema **AnimaLink** ya está construido, probado y funcionando (10 módulos, seguridad por rol, Docker, Swagger, HATEOAS, tests). Lo que queda son cosas **ligadas a tu persona/cuentas** o decisiones académicas que **no puedo hacer por ti**. Aquí están, ordenadas por importancia.

---

## 🔴 1. Subir el proyecto a GitHub (OBLIGATORIO en la rúbrica)
La rúbrica exige repositorio con **avance progresivo, participación de todos los integrantes e historial de cambios**. Esto depende de tu cuenta y la de tu equipo.

Pasos:
1. Crea un repositorio en https://github.com (privado o público, como pida el profe).
2. En la carpeta `kiri/AnimaLink-main`, inicializa git y sube:
   ```bash
   cd kiri/AnimaLink-main
   git init
   git add .
   git commit -m "Estructura inicial del sistema AnimaLink"
   git branch -M main
   git remote add origin https://github.com/TU_USUARIO/TU_REPO.git
   git push -u origin main
   ```
3. **Para evidenciar avance y participación** (lo pide la rúbrica):
   - Trabajen con **ramas** (ej. `feature/mascotas`, `feature/seguridad`) y hagan `merge` con Pull Requests.
   - **Cada integrante** debe hacer commits desde su propia cuenta (que se vea la participación de todos).
   - Hagan **commits frecuentes** con mensajes claros, no todo en uno.

> ⚠️ Yo NO subí nada a GitHub (me pidieron no commitear). El historial lo tienen que generar ustedes.

---

## 🟠 2. Confirmar el número de microservicios con el profesor
El Word dice "mínimo **10 microservicios**". Ahora hay **9 de negocio + el gateway**.
- Pregunta si el gateway cuenta o si exige **10 de negocio** estrictos.
- Si exige 10 de negocio, se puede agregar 1 más (ej. `pago_service` o `notificacion_service`) con el mismo patrón. **(Esto sí lo puede hacer quien te ayuda.)**

---

## 🟠 3. Despliegue con URL pública (si tu evaluación lo pide)
La rúbrica menciona dejar el sistema "disponible mediante una URL". Esto usa **tus cuentas**:
- **Opción fácil (local + túnel):** levanta todo con Docker (`docker compose up`) y expón el gateway con **ngrok**:
  ```bash
  ngrok http 8080
  ```
  Te da una URL pública temporal (necesitas cuenta gratis en https://ngrok.com).
- **Opción nube (gratis):** Railway, Render o similar (requieren tu cuenta y conectar el repo de GitHub).

---

## 🟡 4. Diagrama ER visual (si lo piden como imagen)
El esquema completo está documentado en `MODELO_DATOS.md` (tablas, columnas, relaciones). Si piden un **diagrama visual**:
- Pega la estructura en https://dbdiagram.io o https://drawsql.app y exporta la imagen, **o**
- Usa **MySQL Workbench** → *Reverse Engineer* sobre las bases `animalink_*` (después de correr los servicios) para generar el diagrama automáticamente.

---

## 🟡 5. Informe / documento del proyecto y presentación
Es un entregable académico que deben redactar ustedes (portada, integrantes, descripción, capturas de Swagger/Postman, etc.). El material técnico ya lo tienes en:
- `REQUISITOS_Y_GUIA.md`, `CONFIGURAR_XAMPP.md`, `DOCKER.md`, `MODELO_DATOS.md`.

---

## 🟢 6. Entender el código para poder defenderlo
En la evaluación probablemente les pregunten cómo funciona. Repasa:
- **Comunicación entre servicios:** carpeta `client/` de cada micro (WebClient).
- **Seguridad:** `auth_service` (genera el JWT) y `security/JwtAuthenticationFilter.java` + `config/SecurityConfig.java` de cada micro (valida el token y exige rol en las escrituras).
- **DTOs con datos ocultos:** carpeta `dto/` y `mapper/` (ej. RUT enmascarado, precio de costo oculto).
- **DataFaker:** `DataLoader.java` de cada micro.

---

## Credenciales de prueba (ya vienen cargadas)
Para probar el login y obtener un token:
| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin123` | ADMIN |
| `vet` | `vet123` | VETERINARIO |
| `recep` | `recep123` | RECEPCIONISTA |

Flujo: `POST http://localhost:8081/api/v1/auth/login` → copia el `token` → mándalo en las peticiones de escritura como header `Authorization: Bearer <token>`.

---

## Resumen: qué está hecho vs. qué te toca
| Ya está hecho (código) | Te toca a ti |
|---|---|
| 10 módulos + tests + Docker + seguridad por rol | Subir a **GitHub** con ramas/commits |
| Swagger, HATEOAS, DTOs, DataFaker | Definir si va un **10º** microservicio |
| BD por servicio + perfiles dev/test | **Desplegar** con URL pública (si lo piden) |
| Documentación técnica | **Informe/presentación** + diagrama ER visual |
