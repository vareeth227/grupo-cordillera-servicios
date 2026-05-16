# Script para iniciar/detener los servicios de Grupo Cordillera integrados

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("start", "stop", "restart", "logs", "status", "clean")]
    [string]$Action = "start"
)

$ProjectRoot = Get-Location
$DockerComposePath = Join-Path $ProjectRoot "docker-compose.yml"
$EnvFile = Join-Path $ProjectRoot ".env"

Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   Grupo Cordillera - Microservicios + Base de Datos             ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Validar que Docker está instalado
try {
    $DockerVersion = docker --version 2>$null
    Write-Host "✓ Docker instalado: $DockerVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker no está instalado. Descargalo en: https://www.docker.com/products/docker-desktop" -ForegroundColor Red
    exit 1
}

# Validar que existe docker-compose.yml
if (-not (Test-Path $DockerComposePath)) {
    Write-Host "✗ No se encontró: $DockerComposePath" -ForegroundColor Red
    exit 1
}

Write-Host "✓ docker-compose.yml encontrado" -ForegroundColor Green

# Crear la red cordillera-net si no existe
Write-Host ""
Write-Host "Creando red de Docker..." -ForegroundColor Yellow
docker network create cordillera-net 2>$null
Write-Host "✓ Red cordillera-net lista" -ForegroundColor Green

Write-Host ""

switch ($Action) {
    "start" {
        Write-Host "Iniciando servicios..." -ForegroundColor Yellow
        Write-Host ""
        docker-compose up -d --build
        
        Write-Host ""
        Write-Host "Esperando a que los servicios se inicien (60 segundos)..." -ForegroundColor Yellow
        Start-Sleep -Seconds 60
        
        Write-Host ""
        & $PSScriptRoot\manage-cordillera.ps1 -Action status
    }
    
    "stop" {
        Write-Host "Deteniendo servicios..." -ForegroundColor Yellow
        docker-compose down
        Write-Host "✓ Servicios detenidos" -ForegroundColor Green
    }
    
    "restart" {
        Write-Host "Reiniciando servicios..." -ForegroundColor Yellow
        docker-compose restart
        Write-Host "✓ Servicios reiniciados" -ForegroundColor Green
        Start-Sleep -Seconds 30
        & $PSScriptRoot\manage-cordillera.ps1 -Action status
    }
    
    "logs" {
        Write-Host "Últimos 50 logs del API Gateway:" -ForegroundColor Yellow
        docker logs --tail 50 api-gateway
    }
    
    "status" {
        Write-Host "Estado de los contenedores:" -ForegroundColor Yellow
        Write-Host ""
        $Containers = docker ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
        Write-Host $Containers
        
        Write-Host ""
        Write-Host "PUERTOS DISPONIBLES:" -ForegroundColor Cyan
        Write-Host "  API Gateway:      http://localhost:9090" -ForegroundColor Green
        Write-Host "  ms-ventas:        http://localhost:9091" -ForegroundColor Green
        Write-Host "  ms-ecommerce:     http://localhost:9092" -ForegroundColor Green
        Write-Host "  ms-inventario:    http://localhost:9093" -ForegroundColor Green
        Write-Host "  ms-financiero:    http://localhost:9094" -ForegroundColor Green
        Write-Host "  ms-clientes:      http://localhost:9095" -ForegroundColor Green
        
        Write-Host ""
        Write-Host "BASES DE DATOS:" -ForegroundColor Cyan
        Write-Host "  db-ventas:        localhost:5432" -ForegroundColor Green
        Write-Host "  db-ecommerce:     localhost:5433" -ForegroundColor Green
        Write-Host "  db-inventario:    localhost:5434" -ForegroundColor Green
        Write-Host "  db-financiero:    localhost:5435" -ForegroundColor Green
        Write-Host "  db-clientes:      localhost:5436" -ForegroundColor Green
    }
    
    "clean" {
        Write-Host "Limpiando contenedores y volúmenes..." -ForegroundColor Yellow
        docker-compose down -v
        Write-Host "✓ Contenedores y volúmenes eliminados" -ForegroundColor Green
    }
}

Write-Host ""
