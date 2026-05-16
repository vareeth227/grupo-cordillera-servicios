# Script para iniciar Docker Desktop y los servicios integrados

Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   Iniciando Docker Desktop y Servicios Grupo Cordillera        ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Verificar si Docker Desktop está instalado
$DockerDesktopPath = "C:\Program Files\Docker\Docker\Docker Desktop.exe"
if (-not (Test-Path $DockerDesktopPath)) {
    Write-Host "✗ Docker Desktop no está instalado." -ForegroundColor Red
    Write-Host ""
    Write-Host "Descargalo aquí: https://www.docker.com/products/docker-desktop" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Una vez instalado y ejecutando, vuelve a correr este script." -ForegroundColor Yellow
    exit 1
}

Write-Host "Iniciando Docker Desktop..." -ForegroundColor Yellow

# Iniciar Docker Desktop
& $DockerDesktopPath
Write-Host "✓ Docker Desktop iniciado" -ForegroundColor Green

Write-Host ""
Write-Host "Esperando a que Docker esté listo (120 segundos)..." -ForegroundColor Yellow
Write-Host ""

# Esperar a que Docker esté listo
$MaxRetries = 60
$Retries = 0

while ($Retries -lt $MaxRetries) {
    $Retries++
    
    try {
        docker ps > $null 2>&1
        Write-Host "✓ Docker está listo!" -ForegroundColor Green
        break
    } catch {
        Write-Host "  [$Retries/$MaxRetries] Esperando..." -ForegroundColor Gray
        Start-Sleep -Seconds 2
    }
}

if ($Retries -ge $MaxRetries) {
    Write-Host "✗ Docker tardó demasiado en responder. Intenta manualmente en el próximo paso." -ForegroundColor Red
}

Write-Host ""
Write-Host "Ahora puedes ejecutar:" -ForegroundColor Cyan
Write-Host "  .\manage-cordillera.ps1 -Action start" -ForegroundColor Yellow
Write-Host ""
