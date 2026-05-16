# Grupo Cordillera — Microservicios + Base de Datos Integrada

Backend completo con **5 microservicios Spring Boot**, **5 bases de datos PostgreSQL**, y **API Gateway**. 

**Versión sin Docker Desktop** - Solo necesita: **Java 23 + Maven + Docker** (mínimo, solo para BDs)

## 📊 Servicios

| Servicio      | Puerto | BD         | Descripción |
|---------------|--------|-----------|-------------|
| **api-gateway** | 9090   | N/A | Enrutador central del sistema |
| **ms-ventas** | 9091   | db_ventas (5432) | Gestión de puntos de venta |
| **ms-ecommerce** | 9092   | db_ecommerce (5433) | Pedidos online |
| **ms-inventario** | 9093   | db_inventario (5434) | Control de stock |
| **ms-financiero** | 9094   | db_financiero (5435) | KPIs e ingresos |
| **ms-clientes** | 9095   | db_clientes (5436) | CRM y autenticación |

## 🚀 Inicio Rápido (Sin Docker Desktop)

### Requisitos
- ✅ Java 23+ (verificar: `java -version`)
- ✅ Maven 3.9+ (verificar: `mvn -v`)
- 🐳 Docker (solo para PostgreSQL - muy ligero, ~500MB)

### Paso 1: Iniciar solo las bases de datos

```powershell
docker-compose -f docker-compose-db-only.yml up -d
```

Espera 30 segundos a que las BDs estén listas.

### Paso 2: Compilar todos los microservicios

```powershell
.\build-all.ps1
```

Esto compila los 6 servicios (5 microservicios + API Gateway) y genera archivos JAR.
Toma **3-5 minutos** la primera vez.

### Paso 3: Ejecutar todos los servicios en paralelo

```powershell
.\run-all.ps1
```

Esto inicia:
- 6 procesos Java (uno por cada servicio)
- Cada uno en su propio puerto
- Genera logs en `./logs/`

### Paso 4: Probar que funciona

```powershell
# Test del API Gateway
curl http://localhost:9090/actuator/health

# Test de cada microservicio
curl http://localhost:9091/actuator/health  # ventas
curl http://localhost:9092/actuator/health  # ecommerce
curl http://localhost:9093/actuator/health  # inventario
curl http://localhost:9094/actuator/health  # financiero
curl http://localhost:9095/actuator/health  # clientes
```

Cada uno debe retornar: `{"status":"UP"}`


## 📁 Estructura

```
.
├── api-gateway/              # Spring Cloud Gateway reactivo
├── ms-clientes/              # Microservicio de clientes (Liquibase)
├── ms-ecommerce/             # Microservicio de ecommerce (Liquibase)
├── ms-inventario/            # Microservicio de inventario
├── ms-financiero/            # Microservicio financiero
├── ms-ventas/                # Microservicio de ventas
├── docker-compose-db-only.yml # ⭐ Solo PostgreSQL (mínimo)
├── build-all.ps1             # 🔨 Compilar todos los JAR
├── run-all.ps1               # ▶️  Ejecutar servicios en paralelo
├── logs/                      # 📋 Logs de cada servicio
└── README.md
```

## 🛠️ Comandos Útiles

### Compilación y Ejecución

```powershell
# Compilar todos los microservicios
.\build-all.ps1

# Ejecutar todos en paralelo
.\run-all.ps1

# Compilar uno específico
cd ms-clientes
mvn clean package -DskipTests
cd ..
```

### Ver Logs

```powershell
# Últimas 50 líneas de un servicio
Get-Content logs/ms-clientes.log -Tail 50

# Seguir logs en vivo (Ctrl+C para detener)
Get-Content logs/api-gateway.log -Wait

# Todos los logs
Get-ChildItem logs/ | ForEach-Object { Write-Host $_.Name; Get-Content $_.FullName | Select-Object -Last 10 }
```

### Detener Servicios

```powershell
# Detener BDs (si no necesitas retiniciar)
docker-compose -f docker-compose-db-only.yml down

# Limpiar todo (incluyendo volúmenes/datos)
docker-compose -f docker-compose-db-only.yml down -v

# Matar un proceso Java por puerto
netstat -ano | findstr "9090"  # Encuentra el PID
taskkill /PID <PID> /F         # Mata el proceso
```

### Conectar a la Base de Datos

```powershell
# Desde psql (si está instalado)
psql -h localhost -U postgres -d db_clientes

# O con docker
docker exec -it db-clientes psql -U postgres -d db_clientes
```

## 🐛 Troubleshooting

### Puerto ya está en uso

```powershell
# Encuentra qué está usando el puerto (ej: 9090)
netstat -ano | findstr "9090"

# Mata el proceso
taskkill /PID <PID_NUMBER> /F

# O cambia el puerto en el script run-all.ps1
```

### Un servicio no inicia

```powershell
# Ver log del servicio
Get-Content logs/ms-clientes.log -Tail 100

# Verificar que las BDs están activas
docker ps | findstr postgres

# Reiniciar BDs
docker-compose -f docker-compose-db-only.yml restart
```

### Las BDs no responden

```powershell
# Reiniciar contenedores
docker-compose -f docker-compose-db-only.yml down
docker-compose -f docker-compose-db-only.yml up -d

# Esperar 30 segundos y luego iniciar servicios
```

### Error de conexión a BD

**Si ves: "FATAL: database XXX does not exist"**

- Las migraciones de Liquibase/Hibernate no corrieron
- Solución: Limpiar volúmenes y reintentar

```powershell
docker-compose -f docker-compose-db-only.yml down -v
docker-compose -f docker-compose-db-only.yml up -d
Start-Sleep -Seconds 30
.\run-all.ps1
```

## 📝 Notas Importantes

- **Espacio en disco**: Solo ~500MB (vs 8GB con Docker Desktop)
- **Memoria**: Microservicios usan ~200MB cada uno
- **Datos persistentes**: Se guardan en volúmenes Docker
- **Puertos**: Todos en localhost, no hay nombre de red
- **Logs**: Archivo por servicio en `./logs/`

## 🔗 Próximos Pasos

1. ✅ Backend + BD funcionando
2. ⏳ Integrar con frontend
3. ⏳ Validar endpoints con Postman/Bruno
4. ⏳ Desplegar en producción (AWS)

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
