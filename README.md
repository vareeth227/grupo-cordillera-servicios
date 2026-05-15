# Grupo Cordillera — Microservicios y API Gateway

Microservicios Spring Boot con Docker Compose optimizado.

## Puertos

| Servicio      | Puerto | URL |
|---------------|--------|-----|
| api-gateway   | 9090   | http://localhost:9090 |
| ms-ventas     | 9091   | http://localhost:9091 |
| ms-ecommerce  | 9092   | http://localhost:9092 |
| ms-inventario | 9093   | http://localhost:9093 |
| ms-financiero | 9094   | http://localhost:9094 |
| ms-clientes   | 9095   | http://localhost:9095 |

## Inicio Rápido

### Con Docker Compose

```bash
# Crear red
docker network create cordillera-net

# Iniciar servicios
docker-compose -f docker-compose-t3micro.yml up --build -d

# Ver estado
docker ps
docker logs -f api-gateway

# Detener
docker-compose -f docker-compose-t3micro.yml down
```

### Tests

```bash
# Ejecutar tests de un microservicio
cd ms-clientes
mvn test

# O todos
mvn clean test
```

## Configuración

`.env.example` contiene las variables:
- `POSTGRES_PASSWORD=postgres`
- `JWT_SECRET=cordillera-jwt-secret-2024-fullstack3-grupo`
- `CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:9090`

Copiar a `.env` y ajustar si es necesario.

## Estructura

```
├── api-gateway/              # Puerta de entrada
├── ms-clientes/              # CRM y autenticación
├── ms-ventas/                # Gestión de ventas
├── ms-ecommerce/             # E-commerce
├── ms-inventario/            # Control de inventario
├── ms-financiero/            # Reportes financieros
├── docker-compose-t3micro.yml # Compose optimizado
└── .env.example              # Variables de entorno
```

## Health Check

```bash
curl http://localhost:9090/actuator/health
curl http://localhost:9095/actuator/health
```

## Requisitos

- Docker Desktop
- Java 17+
- Maven 3.8+


**Paso 3 — Ver el estado de los contenedores**
```powershell
docker ps
```

**Paso 4 — Verificar que el Gateway responde**

Abre el navegador en: http://localhost:9090/actuator/health  
Debe mostrar: `{"status":"UP"}`

**Detener**
```powershell
docker-compose -f docker-compose-services.yml down
```

---

## Opción B — Maven directo (sin Docker)

Necesitas **6 terminales PowerShell** abiertas. Ejecuta en cada una desde la carpeta raíz de este repositorio:

**Terminal 1**
```powershell
cd ms-ventas ; mvn spring-boot:run
```

**Terminal 2**
```powershell
cd ms-ecommerce ; mvn spring-boot:run
```

**Terminal 3**
```powershell
cd ms-inventario ; mvn spring-boot:run
```

**Terminal 4**
```powershell
cd ms-financiero ; mvn spring-boot:run
```

**Terminal 5**
```powershell
cd ms-clientes ; mvn spring-boot:run
```

**Terminal 6 — iniciar al final**
```powershell
cd api-gateway ; mvn spring-boot:run
```

O usa el script automático que abre todas las ventanas:
```powershell
.\start-dev.ps1
```

---

## Verificación de endpoints

Una vez que todos los servicios están activos, prueba en el navegador:

| Servicio    | URL de prueba                                        |
|-------------|------------------------------------------------------|
| Gateway     | http://localhost:9090/actuator/health                |
| Ventas      | http://localhost:9091/ventas/transacciones            |
| Ecommerce   | http://localhost:9092/ecommerce/pedidos              |
| Inventario  | http://localhost:9093/inventario/alertas             |
| Financiero  | http://localhost:9094/financiero/ingresos            |
| Clientes    | http://localhost:9095/clientes/activos               |

---

## Pipeline CI/CD

Este repositorio tiene un pipeline de integración continua con **GitHub Actions** que se ejecuta automáticamente en cada `push` a `main`.

El pipeline compila en paralelo los 6 servicios (`mvn package`) y muestra un ✅ si todos pasan o ❌ si alguno falla. El badge en la parte superior de este README refleja el estado del último build.

Ver historial: [Actions → CI — Microservicios](https://github.com/vareeth227/grupo-cordillera-servicios/actions/workflows/ci.yml)

---

## Detener (Opción B)

```powershell
.\stop-dev.ps1
```
Luego cierra las ventanas PowerShell de cada servicio.
