# Grupo Cordillera — Backend (Microservicios + Base de Datos)

Backend completo del sistema Grupo Cordillera: **Eureka Server**, **5 microservicios Spring Boot**, **5 bases de datos PostgreSQL** y **API Gateway** levantados con un solo comando Docker.

---

## Arquitectura

```
                        ┌─────────────────────────────┐
  Frontend :5173  ──►   │   API Gateway  :9090         │  ◄── BFF (Backend for Frontend)
                        │   (Spring Cloud Gateway)     │
                        └────────────┬────────────────┘
                                     │ enruta según /api/{servicio}/**
              ┌──────────┬───────────┼───────────┬──────────┐
              ▼          ▼           ▼           ▼          ▼
         ms-ventas  ms-ecommerce ms-inventario ms-financiero ms-clientes
          :9091       :9092        :9093         :9094        :9095
              │          │           │             │          │
           db_ventas db_ecommerce db_inventario db_financiero db_clientes
            :5432     :5433        :5434          :5435       :5436

  Todos los servicios se registran en:
        ┌─────────────────────────────┐
        │   Eureka Server  :8761      │  ← Service Discovery
        └─────────────────────────────┘
```

Cada microservicio tiene su propia base de datos PostgreSQL. El API Gateway actúa como **BFF (Backend for Frontend)** — único punto de entrada desde el frontend. Eureka Server gestiona el registro y descubrimiento de servicios.

---

## Requisitos

| Herramienta | Versión mínima | Verificar |
|---|---|---|
| Docker Desktop | 4.x | `docker --version` |
| Docker Compose | incluido en Docker Desktop | `docker compose version` |

> **Sin Docker Desktop:** ver sección [Modo sin Docker Desktop](#modo-sin-docker-desktop) al final.

---

## Inicio rápido con Docker

### Paso 1 — Clonar el repositorio

```powershell
git clone https://github.com/vareeth227/grupo-cordillera-servicios.git
cd grupo-cordillera-servicios
```

### Paso 2 — Levantar todo con un solo comando

```powershell
docker-compose up -d --build
```

Este comando hace todo automáticamente:
- Crea las 5 bases de datos PostgreSQL
- Levanta el **Eureka Server** (Service Discovery)
- Compila e inicia los 5 microservicios (esperan a que Eureka esté sano)
- Levanta el API Gateway
- Espera a que cada BD esté sana antes de iniciar su microservicio (healthchecks)

> La primera vez tarda **5-10 minutos** porque descarga las imágenes y compila Java.
> Las siguientes veces: **1-2 minutos**.

### Paso 3 — Verificar que todo está corriendo

```powershell
docker-compose ps
```

Todos los servicios deben aparecer como `healthy` o `running`.

```powershell
# Verificar el API Gateway (punto de entrada principal)
curl http://localhost:9090/actuator/health
```

Respuesta esperada: `{"status":"UP"}`

### Paso 4 — Probar los microservicios

Desde el navegador o Postman:

| Servicio | URL directa | A través del Gateway |
|---|---|---|
| **Eureka Server** | `http://localhost:8761` | — |
| API Gateway | `http://localhost:9090/actuator/health` | — |
| Swagger Gateway | `http://localhost:9090/swagger-ui.html` | — |
| Ventas | `http://localhost:9091/swagger-ui.html` | `http://localhost:9090/api/ventas/transacciones` |
| Ecommerce | `http://localhost:9092/swagger-ui.html` | `http://localhost:9090/api/ecommerce/pedidos` |
| Inventario | `http://localhost:9093/swagger-ui.html` | `http://localhost:9090/api/inventario/alertas` |
| Financiero | `http://localhost:9094/swagger-ui.html` | `http://localhost:9090/api/financiero/ingresos` |
| Clientes | `http://localhost:9095/swagger-ui.html` | `http://localhost:9090/api/clientes/activos` |

---

## Variables de entorno

El `docker-compose.yml` usa variables con valores por defecto. Para personalizarlos, crea un archivo `.env` en la raíz del proyecto:

```env
# Credenciales PostgreSQL (opcional, por defecto: postgres/postgres)
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Secreto JWT compartido entre ms-clientes y api-gateway
JWT_SECRET=cordillera-jwt-secret-2024-fullstack3-grupo

# Orígenes CORS permitidos (separados por coma)
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000
```

> El archivo `.env` está en `.gitignore` y nunca se sube al repositorio.

---

## Escenario dos PCs (evaluación)

El sistema está diseñado para correr en **2 PCs de la misma red LAN**:
- **PC-B (esta):** corre las BDs, Eureka Server, los 5 microservicios y el API Gateway
- **PC-A:** corre solo el frontend

### PC-B — Levantar el backend completo

```powershell
# 1. Obtener la IP de esta máquina (busca "Dirección IPv4")
ipconfig
# Ejemplo: 192.168.1.135

# 2. Levantar todo
docker-compose up -d --build
```

### PC-A — Levantar el frontend apuntando a PC-B

```powershell
# En la PC del frontend, crear o editar frontend/.env
# Reemplaza 192.168.1.135 con la IP real de PC-B
VITE_API_GATEWAY_URL=http://192.168.1.135:9090

# Levantar el frontend
cd frontend
docker-compose up -d --build
# O sin Docker:
npm install && npm run dev
```

El CORS del API Gateway ya incluye `http://192.168.1.*:*` para aceptar peticiones de la red local.

> **Verificar conectividad:** desde PC-A ejecutar
> `curl http://192.168.1.135:9090/actuator/health`
> Debe responder `{"status":"UP"}`

---

## Comandos útiles

```powershell
# Ver logs de todos los servicios en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f api-gateway
docker-compose logs -f ms-clientes

# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes (borra datos de BD)
docker-compose down -v

# Reiniciar un solo servicio
docker-compose restart ms-ventas

# Reconstruir y reiniciar un servicio tras cambios en el código
docker-compose up -d --build ms-ventas
```

---

## Estructura del proyecto

```
grupo-cordillera-servicios/
├── eureka-server/            # Spring Cloud Netflix Eureka Server (:8761)
├── api-gateway/              # Spring Cloud Gateway + Resilience4j + BFF (:9090)
│   └── src/main/java/.../
│       ├── config/
│       │   ├── GatewayConfig.java      # Rutas + CORS + Circuit Breaker
│       │   └── CorsConfig.java         # Configuración CORS global
│       ├── security/
│       │   ├── JwtAuthFilter.java      # Filtro JWT en cada request
│       │   └── JwtUtil.java            # Utilidades JWT
│       └── controller/
│           └── FallbackController.java # Respuestas cuando un MS falla
├── ms-ventas/                # Puntos de venta y transacciones (:9091)
├── ms-ecommerce/             # Pedidos online (:9092)
├── ms-inventario/            # Stock y productos (:9093) — Factory Pattern
├── ms-financiero/            # KPIs financieros (:9094)
├── ms-clientes/              # CRM + autenticación JWT (:9095)
├── docker-compose.yml        # BD + Eureka + microservicios en un solo archivo
├── docker-compose-db-only.yml # Solo PostgreSQL (para desarrollo sin Docker)
├── docker-compose-services.yml # Solo microservicios (BDs externas vía DB_HOST)
├── build-all.ps1             # Compila todos los JAR con Maven
├── run-all.ps1               # Ejecuta los JAR directamente
└── start-complete.ps1        # Levanta todo el stack completo
```

---

## Patrones de diseño implementados

### Repository Pattern
Cada microservicio define interfaces que extienden `JpaRepository`. Spring genera las consultas automáticamente por el nombre del método:
```java
List<Transaccion> findByPuntoDeVentaId(Long id);
List<Pedido> findByEstado(String estado);
```

### Factory Method
Cada microservicio tiene un `*Factory` que centraliza la conversión entre DTOs y entidades JPA:
```
DTO recibido → Factory.crear*() → Entidad JPA → Base de datos
BD           → Factory.toDTO() → DTO           → Response JSON
```

### Circuit Breaker (Resilience4j)
Configurado en el API Gateway para cada microservicio:
- Abre el circuito si el 50% de llamadas fallan
- Espera 10 segundos antes de reintentar
- Devuelve HTTP 503 con mensaje claro si el servicio no responde

### Service Discovery (Spring Cloud Netflix Eureka)
Cada microservicio se registra automáticamente en el Eureka Server al arrancar:
- Dashboard en `http://localhost:8761` muestra todos los servicios registrados
- Variable `EUREKA_HOST` permite configurar la dirección del servidor en Docker

### API Documentation (SpringDoc OpenAPI / Swagger)
Cada microservicio expone su documentación interactiva:
- Swagger UI: `http://localhost:{puerto}/swagger-ui.html`
- JSON schema: `http://localhost:{puerto}/v3/api-docs`

---

## Pruebas unitarias

Ejecutar todos los tests:
```powershell
# Desde la raíz del proyecto
mvn test --projects ms-ventas,ms-ecommerce,ms-inventario,ms-financiero,ms-clientes,api-gateway
```

Ejecutar tests de un servicio específico:
```powershell
cd ms-ecommerce
mvn test
```

Cobertura por módulo:

| Módulo | Tests | Tipo |
|---|---|---|
| ms-ventas | 12 | Unitarios con Mockito (VentaServiceImpl) |
| ms-ecommerce | 6 | Unitarios con Mockito (PedidoServiceImpl) |
| ms-inventario | 14 | Unitarios con Mockito (InventarioServiceImpl) |
| ms-financiero | 12 | Unitarios con Mockito (FinancieroServiceImpl) |
| ms-clientes | 14 | Unitarios con Mockito (ClienteServiceImpl) |
| api-gateway | 1 | Integración (@SpringBootTest) |
| eureka-server | — | Cobertura vía integración |

---

## Modo sin Docker Desktop

Si no tienes Docker Desktop, puedes correr solo las BDs en Docker y los microservicios directamente con Maven.

### Requisitos adicionales
- Java 21+ (`java -version`)
- Maven 3.9+ (`mvn -v`)
- Docker (sin Docker Desktop, solo el engine)

### Paso 1 — Solo las bases de datos

```powershell
docker-compose -f docker-compose-db-only.yml up -d
```

Espera 30 segundos a que las BDs inicialicen.

### Paso 2 — Compilar todos los microservicios

```powershell
.\build-all.ps1
```

Tarda 3-5 minutos la primera vez.

### Paso 3 — Iniciar los servicios

**Opción A — Script automático (abre una ventana por servicio):**
```powershell
.\start-dev.ps1
```

**Opción B — Manual (6 terminales separadas):**
```powershell
# Terminal 1
cd ms-ventas; mvn spring-boot:run

# Terminal 2
cd ms-ecommerce; mvn spring-boot:run

# Terminal 3
cd ms-inventario; mvn spring-boot:run

# Terminal 4
cd ms-financiero; mvn spring-boot:run

# Terminal 5
cd ms-clientes; mvn spring-boot:run

# Terminal 6 — iniciar ÚLTIMO
cd api-gateway; mvn spring-boot:run
```

### Detener servicios (modo sin Docker Desktop)

```powershell
.\stop-dev.ps1
```

---

## Troubleshooting

| Problema | Causa probable | Solución |
|---|---|---|
| Puerto X ya en uso | Otro proceso ocupa el puerto | `netstat -ano \| findstr "9090"` → `taskkill /PID <N> /F` |
| Servicio en `unhealthy` | BD aún inicializando | `docker-compose logs postgres-ventas` — esperar o reiniciar |
| `FATAL: database does not exist` | Volumen corrupto | `docker-compose down -v` y volver a levantar |
| `Connection refused` al gateway | Microservicio no levantó | `docker-compose logs ms-clientes` para ver el error |
| Build falla en Maven | Falta Java/Maven | Verificar `java -version` (necesita 21+) |
