# Grupo Cordillera — Microservicios y API Gateway

[![CI — Microservicios](https://github.com/vareeth227/grupo-cordillera-servicios/actions/workflows/ci.yml/badge.svg)](https://github.com/vareeth227/grupo-cordillera-servicios/actions/workflows/ci.yml)

Contiene los 5 microservicios Spring Boot y el API Gateway con Circuit Breaker y JWT.

> Las **bases de datos deben estar corriendo** antes de iniciar este repositorio.  
> Ver: [grupo-cordillera-db](https://github.com/vareeth227/grupo-cordillera-db)

---

## Requisitos

| Herramienta | Versión mínima | Verificar |
|---|---|---|
| Docker Desktop | cualquiera | `docker --version` |
| Java JDK | 17 | `java -version` |
| Maven | 3.8+ | `mvn -version` |

---

## Puertos

| Servicio      | Puerto | Descripción                          |
|---------------|--------|--------------------------------------|
| api-gateway   | 9090   | Punto de entrada único (enruta todo) |
| ms-ventas     | 9091   | Puntos de venta y transacciones      |
| ms-ecommerce  | 9092   | Pedidos online                       |
| ms-inventario | 9093   | Catálogo de productos y stock        |
| ms-financiero | 9094   | Ingresos, egresos y KPIs             |
| ms-clientes   | 9095   | CRM y tickets de atención            |

---

## Opción A — Docker Compose (recomendado para evaluación)

Abre **PowerShell** en esta carpeta.

**Paso 1 — Crear la red Docker compartida**
```powershell
docker network create cordillera-net
```
> Si aparece "already exists", continúa al paso 2.

**Paso 2 — Construir e iniciar todos los servicios**
```powershell
docker-compose -f docker-compose-services.yml up --build -d
```
> La primera vez tarda entre 5 y 10 minutos mientras Maven descarga dependencias y compila.

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
