# Grupo Cordillera — Backend (Microservicios + Base de Datos)

Backend completo del sistema Grupo Cordillera: **5 microservicios Spring Boot**, **5 bases de datos PostgreSQL** y **API Gateway** levantados con un solo comando Docker.

---

## Arquitectura

```
                        ┌─────────────────────────────┐
  Frontend :5173  ──►   │   API Gateway  :9090         │
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
```

Cada microservicio tiene su propia base de datos PostgreSQL. El API Gateway es el único punto de entrada desde el exterior.

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
- Compila e inicia los 5 microservicios
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
| API Gateway | `http://localhost:9090/actuator/health` | — |
| Ventas | `http://localhost:9091/actuator/health` | `http://localhost:9090/api/ventas/transacciones` |
| Ecommerce | `http://localhost:9092/actuator/health` | `http://localhost:9090/api/ecommerce/pedidos` |
| Inventario | `http://localhost:9093/actuator/health` | `http://localhost:9090/api/inventario/alertas` |
| Financiero | `http://localhost:9094/actuator/health` | `http://localhost:9090/api/financiero/ingresos` |
| Clientes | `http://localhost:9095/actuator/health` | `http://localhost:9090/api/clientes/activos` |

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

## Escenario dos PCs (presentación)

Si el **frontend corre en otra PC** de la misma red:

**En esta PC (backend):**
```powershell
# 1. Obtener la IP de esta máquina
ipconfig
# Busca: "Dirección IPv4" → ejemplo: 192.168.1.135

# 2. Levantar el backend (sin cambios)
docker-compose up -d
```

**En la PC del frontend:**
Configurar `BACKEND_HOST=192.168.1.135` en el docker-compose del frontend.

El CORS del API Gateway ya acepta `http://192.168.1.*:*` sin cambios.

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
├── api-gateway/              # Spring Cloud Gateway + Resilience4j
│   └── src/main/java/.../
│       ├── config/
│       │   └── GatewayConfig.java     # Rutas + CORS + Circuit Breaker
│       └── controller/
│           └── FallbackController.java # Respuestas cuando un MS falla
├── ms-ventas/                # Puntos de venta y transacciones (:9091)
├── ms-ecommerce/             # Pedidos online (:9092)
├── ms-inventario/            # Stock y productos (:9093)
├── ms-financiero/            # KPIs financieros (:9094)
├── ms-clientes/              # CRM + autenticación JWT (:9095)
├── docker-compose.yml        # BD + microservicios en un solo archivo
├── docker-compose-db-only.yml # Solo PostgreSQL (para desarrollo sin Docker)
├── build-all.ps1             # Compila todos los JAR con Maven
├── run-all.ps1               # Ejecuta los JAR directamente
├── start-dev.ps1             # Abre terminales PowerShell por servicio
└── stop-dev.ps1              # Detiene todos los procesos Java
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
| ms-ventas | 3 | Unitarios (lógica de cálculo) |
| ms-ecommerce | 6 | Unitarios con Mockito (PedidoServiceImpl) |
| ms-inventario | 1 | Integración (@SpringBootTest + H2) |
| ms-financiero | 3 | Unitarios (cálculo de KPIs) |
| ms-clientes | 3 | Unitarios (validación de datos) |
| api-gateway | 3 | Unitarios (rutas y CORS) |

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
