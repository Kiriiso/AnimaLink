# Cómo configurar XAMPP (MySQL) para AnimaLink — paso a paso

Esta guía te lleva de cero a tener las **9 bases de datos** que necesita el proyecto.
No necesitas saber SQL: solo copiar y pegar.

---

## Paso 1 — Descargar e instalar XAMPP

1. Entra a 👉 https://www.apachefriends.org/es/index.html
2. Descarga **XAMPP para Windows** (el que trae PHP 8.x).
3. Ejecuta el instalador.
   - Si Windows muestra una advertencia de UAC/antivirus, dale **Permitir**.
   - En la pantalla de componentes, basta con dejar marcados **Apache**, **MySQL** y **phpMyAdmin**.
   - Instálalo en la ruta por defecto `C:\xampp`.
4. Al terminar, abre el **XAMPP Control Panel**.

> 💡 AnimaLink solo necesita **MySQL**. Apache/phpMyAdmin se usan para administrar la base
> de datos cómodamente desde el navegador, pero no son obligatorios para que corran los microservicios.

---

## Paso 2 — Iniciar MySQL (y Apache)

En el **XAMPP Control Panel**:

1. Pulsa **Start** en la fila **Apache** → debe ponerse verde.
2. Pulsa **Start** en la fila **MySQL** → debe ponerse verde.

```
 Module    PID(s)   Port(s)   Actions
 Apache    xxxxx    80, 443   [Stop] [Admin] [Config] [Logs]
 MySQL     xxxxx    3306      [Stop] [Admin] [Config] [Logs]   ← debe quedar VERDE
```

- El puerto de MySQL debe ser **3306** (es el que usan los microservicios).

> ⚠️ Si MySQL **no inicia** o se pone rojo, ve a la sección **Problemas comunes** más abajo
> (casi siempre es porque el puerto 3306 está ocupado por otro MySQL).

---

## Paso 3 — Abrir phpMyAdmin

1. En el XAMPP Control Panel, en la fila **MySQL**, pulsa **Admin**.
   - O abre el navegador en 👉 http://localhost/phpmyadmin
2. Se abrirá phpMyAdmin. El usuario por defecto es **`root`** **sin contraseña**.

---

## Paso 4 — Crear las 9 bases de datos (copiar y pegar)

1. En phpMyAdmin, arriba, pulsa la pestaña **SQL**.
2. Pega **exactamente** esto y pulsa **Continuar / Go**:

```sql
CREATE DATABASE IF NOT EXISTS animalink_auth         CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_usuario      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_cliente      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_mascota      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_cita         CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_historial    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_control_alta CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_inventario   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_factura      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. A la izquierda deberías ver ahora las 9 bases `animalink_*`.

> ✅ **No tienes que crear las tablas a mano.** Cada microservicio crea sus propias tablas
> automáticamente al arrancar (gracias a `spring.jpa.hibernate.ddl-auto=update`).
> Tú solo creas las bases vacías; Spring hace el resto.

> 💡 Cada microservicio además tiene `createDatabaseIfNotExist=true` en su configuración, así que
> incluso podría crear su base solo. Pero es más ordenado crearlas tú con el SQL de arriba.

---

## Paso 5 — Verificar que coincide con la configuración de los microservicios

Cada servicio se conecta con estos datos (archivo `…/src/main/resources/application-dev.properties`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/animalink_cliente?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```

- **host**: `localhost`  **puerto**: `3306`
- **usuario**: `root`
- **contraseña**: *(vacía)*  ← así viene XAMPP por defecto

| Microservicio | Base de datos que usa |
|---|---|
| auth_service | animalink_auth |
| usuario_service | animalink_usuario |
| cliente_service | animalink_cliente |
| mascota_service | animalink_mascota |
| cita_service | animalink_cita |
| historial_service | animalink_historial |
| control_alta_service | animalink_control_alta |
| inventario_service | animalink_inventario |
| factura_service | animalink_factura |

### ¿Tu MySQL de XAMPP TIENE contraseña?
Si en algún momento le pusiste contraseña a `root`, debes ponerla en **cada** `application-dev.properties`:

```properties
spring.datasource.password=TU_CONTRASEÑA
```

---

## Paso 6 — Probar la conexión

1. Asegúrate de que **MySQL está verde** en XAMPP.
2. Levanta un microservicio, por ejemplo:
   ```powershell
   cd kiri\AnimaLink-main\cliente_service
   .\mvnw.cmd spring-boot:run
   ```
3. Si todo está bien verás en la consola algo como `Tomcat started on port 8083` y, en phpMyAdmin,
   la base `animalink_cliente` tendrá una tabla `clientes` con datos de ejemplo (creados por DataFaker).

---

## Problemas comunes en XAMPP

| Síntoma | Causa | Solución |
|---|---|---|
| MySQL no inicia / se pone rojo | El puerto **3306** ya está ocupado (tienes otro MySQL instalado como servicio de Windows) | Abre *Servicios* de Windows, detén el servicio **MySQL**; o en XAMPP → MySQL → **Config → my.ini** cambia el puerto y ajusta los `application-dev.properties` |
| `Access denied for user 'root'@'localhost'` | `root` tiene contraseña | Pon la contraseña en `application-dev.properties` o quítasela en phpMyAdmin (Cuentas de usuario) |
| `Unknown database 'animalink_x'` | No creaste las bases | Repite el **Paso 4** |
| `Communications link failure` al iniciar un servicio | MySQL está apagado | Inicia **MySQL** en XAMPP (Paso 2) |
| Apache no inicia (puerto 80) | Skype/IIS usan el puerto 80 | No es necesario para AnimaLink; puedes dejar Apache apagado y entrar a phpMyAdmin solo si lo necesitas |

---

## Resumen ultra rápido

1. Instala XAMPP → abre el Control Panel.
2. **Start** en MySQL (y Apache).
3. **Admin** (phpMyAdmin) → pestaña **SQL** → pega el bloque del **Paso 4** → Continuar.
4. Listo: 9 bases creadas. Levanta los microservicios y las tablas se crean solas.
