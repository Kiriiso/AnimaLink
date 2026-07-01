# AnimaLink — Modelo de datos (esquema, tablas y columnas)

Cada microservicio tiene su **propia base de datos MySQL independiente** (no se comparten tablas).
Las tablas se crean automáticamente al iniciar cada servicio (`spring.jpa.hibernate.ddl-auto=update`).

**Convención de nombres:** las columnas se generan en `snake_case` (ej: `fechaNacimiento` → `fecha_nacimiento`).
Tipos MySQL usados: `BIGINT` (id), `VARCHAR` (texto/enum), `INT`, `DECIMAL(p,s)`, `DATE`, `DATETIME(6)`, `BIT/TINYINT` (boolean).

> **Relaciones entre microservicios:** NO existen claves foráneas físicas entre bases de datos distintas.
> La relación se guarda como un **id de referencia** (ej: `mascota.cliente_id` apunta a un cliente que vive en
> `animalink_cliente`) y se **valida por HTTP (WebClient)** al crear/consultar. Esto respeta el desacople de microservicios.

---

## 1. auth_service → base de datos `animalink_auth`

### Tabla `usuario_auth`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| username | VARCHAR(255) | NOT NULL, UNIQUE | Nombre de usuario para login |
| password | VARCHAR(255) | NOT NULL | Contraseña **encriptada con BCrypt** |
| rol | VARCHAR(255) | NOT NULL | Enum: `ADMIN`, `VETERINARIO`, `RECEPCIONISTA` |
| activo | BIT | NOT NULL | Cuenta habilitada |
| created_at | DATETIME(6) | | Fecha de creación |

---

## 2. usuario_service → base de datos `animalink_usuario`

### Tabla `usuarios`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| rut | VARCHAR(15) | NOT NULL, UNIQUE | RUT (se enmascara en las respuestas) |
| nombre | VARCHAR(255) | NOT NULL | Nombre |
| apellido | VARCHAR(255) | NOT NULL | Apellido |
| email | VARCHAR(255) | NOT NULL, UNIQUE | Correo institucional |
| telefono | VARCHAR(20) | | Teléfono |
| rol | VARCHAR(255) | NOT NULL | Enum: `ADMIN`, `VETERINARIO`, `RECEPCIONISTA` |
| especialidad | VARCHAR(255) | | Solo aplica a veterinarios |
| activo | BIT | NOT NULL | Usuario habilitado |
| created_at | DATETIME(6) | | Fecha de creación |

---

## 3. cliente_service → base de datos `animalink_cliente`

### Tabla `clientes`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| rut | VARCHAR(15) | NOT NULL, UNIQUE | RUT (sensible, se enmascara) |
| nombre | VARCHAR(255) | NOT NULL | Nombre |
| apellido | VARCHAR(255) | NOT NULL | Apellido |
| email | VARCHAR(255) | NOT NULL, UNIQUE | Correo |
| telefono | VARCHAR(20) | | Teléfono |
| direccion | VARCHAR(255) | | Dirección |
| activo | BIT | NOT NULL | Cliente activo |
| created_at | DATETIME(6) | | Fecha de registro |

---

## 4. mascota_service → base de datos `animalink_mascota`

### Tabla `mascotas`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| nombre | VARCHAR(255) | NOT NULL | Nombre de la mascota |
| especie | VARCHAR(255) | NOT NULL | Enum: `PERRO`, `GATO`, `AVE`, `ROEDOR`, `REPTIL`, `OTRO` |
| raza | VARCHAR(255) | | Raza |
| sexo | VARCHAR(255) | NOT NULL | Enum: `MACHO`, `HEMBRA` |
| fecha_nacimiento | DATE | | Fecha de nacimiento |
| color | VARCHAR(255) | | Color |
| cliente_id | BIGINT | NOT NULL | **Referencia** al cliente dueño (en `animalink_cliente`) |
| activo | BIT | NOT NULL | Mascota activa |
| created_at | DATETIME(6) | | Fecha de registro |

**Relación:** `mascota.cliente_id → cliente.id` (validada vía WebClient contra cliente_service).

---

## 5. cita_service → base de datos `animalink_cita`

### Tabla `citas`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| mascota_id | BIGINT | NOT NULL | **Referencia** a la mascota (en `animalink_mascota`) |
| veterinario_id | BIGINT | NOT NULL | **Referencia** al veterinario (en `animalink_usuario`) |
| fecha_hora | DATETIME(6) | NOT NULL | Fecha y hora de la cita |
| motivo | VARCHAR(255) | NOT NULL | Motivo de la consulta |
| estado | VARCHAR(255) | NOT NULL | Enum: `PROGRAMADA`, `CONFIRMADA`, `ATENDIDA`, `CANCELADA` |
| observaciones | VARCHAR(500) | | Observaciones |
| created_at | DATETIME(6) | | Fecha de creación |

**Relaciones:** `cita.mascota_id → mascota.id` y `cita.veterinario_id → usuario.id`
(ambas validadas vía WebClient; además se valida que el usuario tenga rol `VETERINARIO`).

---

## 6. historial_service → base de datos `animalink_historial`

### Tabla `historiales`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| mascota_id | BIGINT | NOT NULL | **Referencia** a la mascota (en `animalink_mascota`) |
| fecha | DATE | NOT NULL | Fecha de la atención |
| diagnostico | VARCHAR(255) | NOT NULL | Diagnóstico |
| tratamiento | VARCHAR(500) | | Tratamiento indicado |
| observaciones | VARCHAR(500) | | Observaciones |
| created_at | DATETIME(6) | | Fecha de creación |

**Relación:** `historial.mascota_id → mascota.id` (validada vía WebClient).

---

## 7. control_alta_service → base de datos `animalink_control_alta`

### Tabla `controles_alta`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| mascota_id | BIGINT | NOT NULL | **Referencia** a la mascota (en `animalink_mascota`) |
| fecha_ingreso | DATETIME(6) | NOT NULL | Fecha/hora de ingreso |
| fecha_alta | DATETIME(6) | | Fecha/hora de alta (null si sigue hospitalizado) |
| motivo_ingreso | VARCHAR(255) | NOT NULL | Motivo del ingreso |
| estado | VARCHAR(255) | NOT NULL | Enum: `HOSPITALIZADO`, `DADO_DE_ALTA` |
| observaciones | VARCHAR(500) | | Observaciones |
| created_at | DATETIME(6) | | Fecha de creación |

**Relación:** `control_alta.mascota_id → mascota.id` (validada vía WebClient).

---

## 8. inventario_service → base de datos `animalink_inventario`

### Tabla `insumos`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| nombre | VARCHAR(255) | NOT NULL | Nombre del insumo |
| categoria | VARCHAR(255) | NOT NULL | Enum: `MEDICAMENTO`, `INSUMO`, `ALIMENTO`, `VACUNA`, `OTRO` |
| descripcion | VARCHAR(255) | | Descripción |
| stock | INT | NOT NULL | Stock disponible |
| stock_minimo | INT | NOT NULL | Stock mínimo de alerta |
| precio_costo | DECIMAL(10,2) | | Precio de costo (**sensible, NO se expone**) |
| precio_venta | DECIMAL(10,2) | NOT NULL | Precio de venta al público |
| fecha_vencimiento | DATE | | Fecha de vencimiento |
| activo | BIT | NOT NULL | Insumo activo |
| created_at | DATETIME(6) | | Fecha de creación |

---

## 9. factura_service → base de datos `animalink_factura`

Este servicio tiene **dos tablas relacionadas dentro de su propia base de datos** (relación 1:N real con FK física).

### Tabla `facturas`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| cliente_id | BIGINT | NOT NULL | **Referencia** al cliente (en `animalink_cliente`) |
| fecha | DATETIME(6) | NOT NULL | Fecha de emisión |
| estado | VARCHAR(255) | NOT NULL | Enum: `EMITIDA`, `PAGADA`, `ANULADA` |
| total | DECIMAL(12,2) | NOT NULL | Total de la factura |
| created_at | DATETIME(6) | | Fecha de creación |

### Tabla `detalles_factura`
| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador |
| factura_id | BIGINT | FK → facturas(id) | Factura a la que pertenece la línea |
| insumo_id | BIGINT | NOT NULL | **Referencia** al insumo (en `animalink_inventario`) |
| descripcion | VARCHAR(255) | | Nombre del insumo (copiado al facturar) |
| cantidad | INT | NOT NULL | Cantidad |
| precio_unitario | DECIMAL(10,2) | NOT NULL | Precio unitario (tomado de inventario_service) |
| subtotal | DECIMAL(12,2) | NOT NULL | cantidad × precio_unitario |

**Relaciones:**
- `detalles_factura.factura_id → facturas.id` (FK **física**, 1:N, dentro de la misma base de datos).
- `factura.cliente_id → cliente.id` y `detalle.insumo_id → insumo.id` (**referencias** a otros microservicios,
  validadas vía WebClient; al facturar se descuenta el stock en inventario_service).

---

## Mapa de relaciones entre microservicios (flujo de datos)

```
                         ┌───────────────┐
                         │ usuario_service│ (veterinarios)
                         └───────▲───────┘
                                 │ veterinario_id (WebClient)
   ┌──────────────┐  cliente_id  │
   │cliente_service◄──────────────┼───────────────┐
   └──────▲───────┘  (WebClient)  │               │ cliente_id (WebClient)
          │ cliente_id            │          ┌─────┴────────┐
          │ (WebClient)     ┌─────┴──────┐   │factura_service│
   ┌──────┴────────┐        │ cita_service│   └─────┬────────┘
   │mascota_service◄────────┤             │         │ insumo_id (WebClient, descuenta stock)
   └──▲───▲───▲────┘ mascota_id           │   ┌─────▼──────────┐
      │   │   │      (WebClient)           │   │inventario_service│
      │   │   └──────────────┐             │   └─────────────────┘
      │   │ mascota_id       │ mascota_id  │
┌─────┴─┐ │ (WebClient)      │ (WebClient) │
│historial│ ┌────────────────┴──┐          │
│_service │ │control_alta_service│          │
└─────────┘ └───────────────────┘          │
                                            │
   Todo pasa por el  ┌───────────┐ ◄────────┘
   API GATEWAY  ────►│  gate-way │ (puerto 8080)
                     └───────────┘
```

- **cita_service** consulta a **mascota_service** (¿existe la mascota?) y a **usuario_service** (¿existe y es VETERINARIO?).
- **mascota_service** consulta a **cliente_service** (¿existe el dueño?).
- **historial_service** y **control_alta_service** consultan a **mascota_service**.
- **factura_service** consulta a **cliente_service** (¿existe el cliente?) y a **inventario_service** (precio + descuento de stock).
- El **gate-way** enruta todas las peticiones `/api/*/recurso/**` al microservicio correspondiente.
