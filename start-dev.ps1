# ============================================================
# Script de inicio del entorno de desarrollo - Grupo Cordillera
# Uso: clic derecho sobre este archivo > "Ejecutar con PowerShell"
#      O desde terminal: .\start-dev.ps1
# ============================================================

$baseDir = "d:\user\Desktop\Fullstack III"

Write-Host ""
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "   GRUPO CORDILLERA - Iniciando entorno de desarrollo" -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# Paso 1: Levantar las bases de datos con Docker
Write-Host "[1/3] Levantando bases de datos PostgreSQL..." -ForegroundColor Yellow
Set-Location $baseDir
docker-compose -f docker-compose-db.yml up -d

Write-Host ""
Write-Host "      Esperando 15 segundos para que las BDs esten listas..." -ForegroundColor Gray
Start-Sleep -Seconds 15

# Paso 2: Abrir una ventana PowerShell por cada microservicio
Write-Host "[2/3] Iniciando microservicios (se abriran 6 ventanas)..." -ForegroundColor Yellow
Write-Host ""

$servicios = @(
    @{ nombre = "ms-ventas";     carpeta = "ms-ventas";     color = "DarkGreen" },
    @{ nombre = "ms-ecommerce";  carpeta = "ms-ecommerce";  color = "DarkBlue" },
    @{ nombre = "ms-inventario"; carpeta = "ms-inventario"; color = "DarkMagenta" },
    @{ nombre = "ms-financiero"; carpeta = "ms-financiero"; color = "DarkRed" },
    @{ nombre = "ms-clientes";   carpeta = "ms-clientes";   color = "DarkCyan" },
    @{ nombre = "api-gateway";   carpeta = "api-gateway";   color = "DarkYellow" }
)

foreach ($svc in $servicios) {
    $carpeta = "$baseDir\$($svc.carpeta)"
    # Abre una nueva ventana de PowerShell con el titulo del servicio
    Start-Process powershell -ArgumentList `
        "-NoExit", `
        "-Command", `
        "cd '$carpeta'; `$host.UI.RawUI.WindowTitle = '$($svc.nombre)'; Write-Host 'Iniciando $($svc.nombre)...' -ForegroundColor Green; mvn spring-boot:run"
    Write-Host "   + Ventana abierta para $($svc.nombre)" -ForegroundColor Green
    Start-Sleep -Seconds 2
}

# Paso 3: Abrir el frontend
Write-Host ""
Write-Host "[3/3] Iniciando frontend React..." -ForegroundColor Yellow
$frontendDir = "$baseDir\frontend"
Start-Process powershell -ArgumentList `
    "-NoExit", `
    "-Command", `
    "cd '$frontendDir'; `$host.UI.RawUI.WindowTitle = 'frontend'; Write-Host 'Iniciando frontend...' -ForegroundColor Cyan; npm install; npm run dev"
Write-Host "   + Ventana abierta para frontend" -ForegroundColor Green

Write-Host ""
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "   Todo iniciado. Espera ~3 minutos la primera vez." -ForegroundColor Cyan
Write-Host "   Dashboard: http://localhost:5173" -ForegroundColor White
Write-Host "   API Gateway: http://localhost:9090" -ForegroundColor White
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Presiona cualquier tecla para cerrar esta ventana..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
