# Lou Araya Macramé — Microservicios

Backend de la app móvil **Lou Araya Macramé** (DSY1105, Duoc UC). Son tres microservicios Spring Boot independientes. Cada uno tiene su propia base de datos H2 (guardada en archivo) y su propio puerto.

App móvil: repositorio **EA2-lou-araya** → https://github.com/andresjp23/EA2-lou-araya

## Integrantes

Andrés Jordan · Diana Romero · Ronald Troncoso

## Servicios

| Servicio | Puerto | Responsabilidad | Base de datos |
|---|---|---|---|
| `ms-usuarios` | 8081 | Registro, login y perfil (contraseñas con hash BCrypt) | `ms-usuarios/data/usuarios` |
| `ms-catalogo` | 8082 | Productos y campañas/colecciones | `ms-catalogo/data/catalogo` |
| `ms-solicitudes` | 8083 | Solicitudes de cotización y su ciclo de estados | `ms-solicitudes/data/solicitudes` |

Todos siguen la misma estructura: `controller` → `service` (interfaz + implementación) → `repository` (Spring Data JPA) → `model`, con DTOs (`record`) validados con Bean Validation y un `@RestControllerAdvice` que devuelve los errores con un formato común:

```json
{ "status": 400, "message": "Datos inválidos", "errors": { "email": "Ingresa un correo válido" }, "timestamp": "..." }
```

Códigos usados: `400` validación · `401` credenciales · `404` no existe · `409` duplicado (correo/alias) · `422` regla de negocio (fechas, transición de estado).

## Requisitos y ejecución

- JDK 17 o superior y Maven 3.9 o superior.

```bash
mvn package                      # compila los 3 servicios y corre las pruebas
java -jar ms-usuarios/target/ms-usuarios-1.0.0.jar
java -jar ms-catalogo/target/ms-catalogo-1.0.0.jar
java -jar ms-solicitudes/target/ms-solicitudes-1.0.0.jar
```

Cada servicio se ejecuta en su propia terminal (o con *Run* desde IntelliJ). Al primer inicio se cargan datos de ejemplo: cuentas, catálogo y campañas.

- **Swagger UI:** `http://localhost:808X/swagger-ui.html`
- **Consola H2:** `http://localhost:808X/h2-console` (JDBC URL `jdbc:h2:file:./data/<servicio>`, usuario `sa`, sin contraseña)
- Para reiniciar los datos: detener los servicios y borrar las carpetas `*/data`.

**Cuentas iniciales:** `admin@louaraya.cl / Admin123` (ADMIN) · `cliente@louaraya.cl / Cliente123` (CLIENT)

## Endpoints

### ms-usuarios (8081)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/usuarios/registro` | Crea una cuenta de cliente (201) |
| POST | `/api/usuarios/login` | Valida credenciales y devuelve el usuario |
| GET | `/api/usuarios` | Lista los usuarios |
| GET | `/api/usuarios/{id}` | Obtiene un usuario |
| PUT | `/api/usuarios/{id}` | Actualiza nombre, alias, teléfono y avatar |
| DELETE | `/api/usuarios/{id}` | Elimina un usuario (204) |

### ms-catalogo (8082)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/productos?disponibles=true` | Lista los productos (opcional: solo los disponibles) |
| GET | `/api/productos/{id}` | Obtiene un producto |
| POST | `/api/productos` | Crea un producto (201) |
| PUT | `/api/productos/{id}` | Actualiza un producto |
| PATCH | `/api/productos/{id}/disponibilidad` | `{"value": false}` activa o desactiva |
| DELETE | `/api/productos/{id}` | Elimina un producto (204) |
| GET | `/api/campanas?activas=true` | Lista las campañas |
| GET | `/api/campanas/{id}` | Obtiene una campaña |
| POST | `/api/campanas` | Crea una campaña (valida que término ≥ inicio) |
| PUT | `/api/campanas/{id}` | Actualiza una campaña |
| PATCH | `/api/campanas/{id}/estado` | `{"value": true}` activa o desactiva |
| DELETE | `/api/campanas/{id}` | Elimina una campaña (204) |

### ms-solicitudes (8083)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/solicitudes?userId=&status=` | Lista las solicitudes (por cliente o por estado) |
| GET | `/api/solicitudes/{id}` | Obtiene una solicitud |
| POST | `/api/solicitudes` | Crea una solicitud (queda `PENDIENTE`) |
| PUT | `/api/solicitudes/{id}` | Edita una solicitud (solo si está `PENDIENTE`) |
| PATCH | `/api/solicitudes/{id}/estado` | `{"status": "APROBADA"}` cambia el estado |
| DELETE | `/api/solicitudes/{id}` | Elimina o cancela una solicitud (204) |

Transiciones permitidas (definidas en el enum `EstadoSolicitud`):
`PENDIENTE → EN_REVISION | APROBADA | RECHAZADA`, `EN_REVISION → APROBADA | RECHAZADA | PENDIENTE`, `APROBADA → COMPLETADA | RECHAZADA`. `RECHAZADA` y `COMPLETADA` son estados finales.

## Pruebas

`mvn test` ejecuta 25 pruebas unitarias de la capa de servicio (JUnit 5 + Mockito): registro duplicado, login, validación de fechas, transiciones de estado, recursos inexistentes, etc.
