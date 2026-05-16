# Script completo para iniciar completamente los servicios integrados

Write-Host "`n╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   GRUPO CORDILLERA - INICIALIZACIÓN COMPLETA                    ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

# Validar requisitos
Write-Host "1️⃣  Validando Docker..." -ForegroundColor Yellow

try {
    $DockerVersion = docker --version
    Write-Host "   ✓ $DockerVersion" -ForegroundColor Green
} catch {
    Write-Host "   ✗ Docker no está disponible o no está en PATH" -ForegroundColor Red
    Write-Host "`n   Instalaciones posibles:" -ForegroundColor Yellow
    Write-Host "   • Docker Desktop: https://www.docker.com/products/docker-desktop" -ForegroundColor Gray
    Write-Host "   • Docker CLI: choco install docker-cli" -ForegroundColor Gray
    exit 1
}

# Validar docker-compose
try {
    $ComposeVersion = docker-compose --version
    Write-Host "   ✓ $ComposeVersion" -ForegroundColor Green
} catch {
    Write-Host "   ✗ docker-compose no está disponible" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "2️⃣  Verificando Docker daemon..." -ForegroundColor Yellow

$MaxRetries = 5
$Retries = 0
$DaemonReady = $false

while ($Retries -lt $MaxRetries) {
    $RetryCount = $Retries + 1
    try {
        $Status = docker ps -q 2>$null
        Write-Host "   ✓ Docker daemon respondiendo correctamente" -ForegroundColor Green
        $DaemonReady = $true
        break
    } catch {
        if ($RetryCount -lt $MaxRetries) {
            Write-Host "   ⏳ Intento $RetryCount/$MaxRetries - Docker no responde aún..." -ForegroundColor Yellow
            Start-Sleep -Seconds 5
        }
        $Retries++
    }
}

if (-not $DaemonReady) {
    Write-Host "   ✗ Docker daemon no responde después de $MaxRetries intentos" -ForegroundColor Red
    Write-Host "`n   Soluciones:" -ForegroundColor Yellow
    Write-Host "   • Abre Docker Desktop desde el menú Inicio" -ForegroundColor Gray
    Write-Host "   • Espera a que cargue completamente (2-3 minutos)" -ForegroundColor Gray
    Write-Host "   • Luego ejecuta este script nuevamente" -ForegroundColor Gray
    Write-Host ""
    exit 1
}

Write-Host ""
Write-Host "3️⃣  Creando red de Docker..." -ForegroundColor Yellow

$Network = $null
try {
    $Network = docker network inspect cordillera-net -f '{{.Name}}' 2>$null
}
catch {
    # Continue
}

if ($Network -eq "cordillera-net") {
    Write-Host "   ✓ Red 'cordillera-net' ya existe" -ForegroundColor Green
} else {
    Write-Host "   • Creando red 'cordillera-net'..." -ForegroundColor Gray
    docker network create cordillera-net | Out-Null
    Write-Host "   ✓ Red creada correctamente" -ForegroundColor Green
}

Write-Host ""
Write-Host "4️⃣  Validando docker-compose.yml..." -ForegroundColor Yellow

$ValidConfig = $false
try {
    docker-compose config > $null 2>&1
    $ValidConfig = $true
    Write-Host "   ✓ Archivo docker-compose.yml válido" -ForegroundColor Green
}
catch {
    Write-Host "   ✗ Errores en docker-compose.yml" -ForegroundColor Red
    docker-compose config
    exit 1
}

if (-not $ValidConfig) {
    exit 1
}

Write-Host ""
Write-Host "5️⃣  Construyendo e iniciando servicios..." -ForegroundColor Yellow
Write-Host "   (Esto puede tomar 3-5 minutos en la primera ejecución)" -ForegroundColor Gray
Write-Host ""

docker-compose up -d --build

if ($LASTEXITCODE -ne 0) {
    Write-Host "   ✗ Error al iniciar servicios" -ForegroundColor Red
    Write-Host ""
    Write-Host "   Intenta: docker-compose logs" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "⏳  Esperando a que los servicios estén saludables..." -ForegroundColor Yellow
Write-Host "   (Timeout: 120 segundos)" -ForegroundColor Gray

$ServicesPorts = @{
    "api-gateway" = 9090
    "ms-ventas" = 9091
    "ms-ecommerce" = 9092
    "ms-inventario" = 9093
    "ms-financiero" = 9094
    "ms-clientes" = 9095
}

$ReadyServices = 0
$TotalServices = $ServicesPorts.Count
$StartTime = Get-Date
$TimeoutSeconds = 120

while ((Get-Date) -lt $StartTime.AddSeconds($TimeoutSeconds)) {
    $ReadyServices = 0
    
    foreach ($Service in $ServicesPorts.Keys) {
        try {
            $Port = $ServicesPorts[$Service]
            $Response = curl -s -m 2 "http://localhost:$Port/actuator/health" -ErrorAction SilentlyContinue
            
            if ($Response -like '*"status":"UP"*' -or $Response -like "*UP*") {
                $ReadyServices++
            }
        } catch {
            # Continuar si no responde
        }
    }
    
    if ($ReadyServices -eq $TotalServices) {
        Write-Host "   ✓ Todos los servicios están saludables!" -ForegroundColor Green
        break
    } else {
        Write-Host "   [$ReadyServices/$TotalServices] servicios listos..." -ForegroundColor Gray
        Start-Sleep -Seconds 5
    }
}

Write-Host ""
Write-Host "6️⃣  Verificación final..." -ForegroundColor Yellow
Write-Host ""

$ContainerStatus = docker ps --format "table {{.Names}}\t{{.Status}}"
Write-Host $ContainerStatus

Write-Host ""
Write-Host "✅ INICIALIZACIÓN COMPLETADA" -ForegroundColor Green
Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║                      INFORMACIÓN DE ACCESO                      ║" -ForegroundColor Green
Write-Host "╠════════════════════════════════════════════════════════════════╣" -ForegroundColor Green
Write-Host "║                                                                ║" -ForegroundColor Green
Write-Host "║  🔗 API Gateway (Punto de entrada principal)                  ║" -ForegroundColor Green
Write-Host "║     http://localhost:9090/actuator/health                     ║" -ForegroundColor Green
Write-Host "║                                                                ║" -ForegroundColor Green
Write-Host "║  📦 Microservicios:                                            ║" -ForegroundColor Green
Write-Host "║     • ms-ventas:      http://localhost:9091/actuator/health   ║" -ForegroundColor Green
Write-Host "║     • ms-ecommerce:   http://localhost:9092/actuator/health   ║" -ForegroundColor Green
Write-Host "║     • ms-inventario:  http://localhost:9093/actuator/health   ║" -ForegroundColor Green
Write-Host "║     • ms-financiero:  http://localhost:9094/actuator/health   ║" -ForegroundColor Green
Write-Host "║     • ms-clientes:    http://localhost:9095/actuator/health   ║" -ForegroundColor Green
Write-Host "║                                                                ║" -ForegroundColor Green
Write-Host "║  🗄️  Bases de Datos (PostgreSQL):                             ║" -ForegroundColor Green
Write-Host "║     • db-ventas:      localhost:5432                           ║" -ForegroundColor Green
Write-Host "║     • db-ecommerce:   localhost:5433                           ║" -ForegroundColor Green
Write-Host "║     • db-inventario:  localhost:5434                           ║" -ForegroundColor Green
Write-Host "║     • db-financiero:  localhost:5435                           ║" -ForegroundColor Green
Write-Host "║     • db-clientes:    localhost:5436                           ║" -ForegroundColor Green
Write-Host "║                                                                ║" -ForegroundColor Green
Write-Host "║  👤 Credenciales:                                              ║" -ForegroundColor Green
Write-Host "║     Usuario: postgres                                          ║" -ForegroundColor Green
Write-Host "║     Contraseña: postgres                                       ║" -ForegroundColor Green
Write-Host "║                                                                ║" -ForegroundColor Green
Write-Host "╠════════════════════════════════════════════════════════════════╣" -ForegroundColor Green
Write-Host "║  Comandos útiles:                                              ║" -ForegroundColor Green
Write-Host "║  • Ver logs:          docker logs -f api-gateway              ║" -ForegroundColor Green
Write-Host "║  • Detener servicios: docker-compose down                     ║" -ForegroundColor Green
Write-Host "║  • Limpiar todo:      .\manage-cordillera.ps1 -Action clean   ║" -ForegroundColor Green
Write-Host "║  • Ver estado:        .\manage-cordillera.ps1 -Action status  ║" -ForegroundColor Green
Write-Host "║                                                                ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
