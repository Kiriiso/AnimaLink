# AnimaLink con Docker — levantar TODO con un comando

Con Docker **no necesitas instalar Java, Maven ni XAMPP**: Docker construye y ejecuta MySQL + los 9 microservicios + el gateway dentro de contenedores.

## Requisito
- **Docker Desktop** instalado y **abierto/corriendo** (icono de la ballena activo). Descarga: https://www.docker.com/products/docker-desktop/

## El comando principal
Ubícate en la carpeta donde está el `docker-compose.yml` (o sea `kiri/AnimaLink-main`) y ejecuta:

```bash
docker compose up --build
```

### ¿Qué hace `docker compose up`?
- **Lee `docker-compose.yml`** y levanta **todos los servicios definidos** ahí a la vez: `mysql`, los 9 microservicios y el `gateway`.
- Crea una **red interna** donde los contenedores se ven por su nombre (por eso el gateway llega a `cliente-service:8083`, etc.).
- Respeta el orden con `depends_on`: primero MySQL (espera a que esté **sano** con su healthcheck) y luego los servicios.
- Deja la consola mostrando **los logs de todos** en vivo.

### Las banderas que le agregamos / útiles
| Comando | Qué hace |
|---|---|
| `docker compose up --build` | **Construye** las imágenes (compila cada servicio) y las levanta. Úsalo la 1ª vez y cuando cambies código. |
| `docker compose up -d` | Las levanta en **segundo plano** (detached); recuperas la terminal. |
| `docker compose up --build -d` | Las dos: reconstruye y en segundo plano. |
| `docker compose logs -f gateway` | Ver los logs de un servicio (ej. gateway) en vivo. |
| `docker compose ps` | Ver qué contenedores están arriba y su estado. |
| `docker compose down` | **Apaga y elimina** los contenedores (los datos de MySQL se conservan). |
| `docker compose down -v` | Apaga **y borra la base de datos** (el volumen `mysql_data`). Empieza de cero. |
| `docker compose stop` / `start` | Pausar / reanudar sin borrar. |

> 💡 La **primera vez** tarda varios minutos: descarga las imágenes base (Java, MySQL) y compila los 9 servicios. Las siguientes veces es mucho más rápido (usa caché).

## ⚠️ Antes de ejecutarlo (evita choques de puertos)
Docker usa los puertos **3306** (MySQL) y **8080–8089**. Si ya tienes algo escuchando ahí, **apágalo primero**:
- **XAMPP/MySQL local** en 3306 → deténlo (o Docker no podrá usar el 3306).
- Cualquier microservicio que hayas levantado con `mvnw` (ej. cliente_service en 8083) → ciérralo (`Ctrl+C` o `taskkill /PID <pid> /F`).

## Qué queda arriba y dónde entrar
| Contenedor | Puerto | Swagger UI |
|---|---|---|
| gateway | 8080 | (enruta a todos) |
| auth-service | 8081 | http://localhost:8081/doc/swagger-ui.html |
| usuario-service | 8082 | http://localhost:8082/doc/swagger-ui.html |
| cliente-service | 8083 | http://localhost:8083/doc/swagger-ui.html |
| mascota-service | 8084 | http://localhost:8084/doc/swagger-ui.html |
| cita-service | 8085 | http://localhost:8085/doc/swagger-ui.html |
| historial-service | 8086 | http://localhost:8086/doc/swagger-ui.html |
| control-alta-service | 8087 | http://localhost:8087/doc/swagger-ui.html |
| inventario-service | 8088 | http://localhost:8088/doc/swagger-ui.html |
| factura-service | 8089 | http://localhost:8089/doc/swagger-ui.html |
| mysql | 3306 | (base de datos) |

A través del **gateway**: `http://localhost:8080/api/v1/clientes`, `http://localhost:8080/api/v1/mascotas`, etc.

## Flujo típico
```bash
# 1. Levantar todo (primera vez)
docker compose up --build

# (en otra terminal) ver que estan arriba
docker compose ps

# 2. Probar por el gateway
#    GET http://localhost:8080/api/v1/clientes

# 3. Apagar cuando termines
docker compose down
```

## Cómo funciona por dentro (para entenderlo)
- Cada microservicio tiene un **`Dockerfile`** multi-etapa: primero compila el `.jar` con JDK 21, luego lo ejecuta en una imagen liviana con solo el JRE 21.
- En el `docker-compose.yml`, a cada servicio le pasamos por **variables de entorno**:
  - `SPRING_DATASOURCE_URL` → apunta a la BD dentro del contenedor `mysql` (no a `localhost`).
  - `SERVICES_*_URL` → apunta a los otros microservicios por su **nombre de contenedor** (ej. `http://cliente-service:8083`), no a `localhost`.
- Spring Boot toma esas variables y **sobrescribe** lo que dice `application-dev.properties` (que usa `localhost` para cuando corres sin Docker).
- MySQL crea las 9 bases con el script `mysql-init/01-init.sql`.

## Comandos de diagnóstico
```bash
docker compose logs -f cliente-service     # ver por que un servicio no arranca
docker compose exec mysql mysql -uroot -proot -e "SHOW DATABASES;"   # ver las bases creadas
docker compose build cliente-service       # reconstruir solo un servicio
```
