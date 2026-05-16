# Script para compilar todos los microservicios
# Requiere: Maven en PATH o usar: .\setup-maven.ps1 primero

$MavenExe = "C:\Program Files\NetBeans-23\netbeans\java\maven\bin\mvn.cmd"

# Si Maven no está en PATH, usar la ruta de NetBeans
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    $env:Path += ";C:\Program Files\NetBeans-23\netbeans\java\maven\bin"
}

Write-Host "`n╔═══════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  Compilando todos los microservicios                          ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

$Services = @("ms-ventas", "ms-ecommerce", "ms-inventario", "ms-financiero", "ms-clientes", "api-gateway")
$FailedServices = @()

foreach ($Service in $Services) {
    Write-Host "📦 Compilando $Service..." -ForegroundColor Yellow
    
    Push-Location $Service
    
    # Limpiar y compilar
    & $MavenExe clean package -DskipTests -q 2>&1 | ForEach-Object {
        if ($_ -match "ERROR") {
            Write-Host "   ⚠️  $_" -ForegroundColor Red
        }
    }
    
    if ($LASTEXITCODE -eq 0) {
        # Contar líneas de código
        $JarFile = Get-ChildItem -Path "target" -Filter "*.jar" -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($JarFile) {
            Write-Host "   ✓ $Service compilado exitosamente" -ForegroundColor Green
            Write-Host "   📄 JAR: $($JarFile.Name) ($([math]::Round($JarFile.Length / 1MB, 2)) MB)" -ForegroundColor Gray
        }
    } else {
        Write-Host "   ✗ Error compilando $Service" -ForegroundColor Red
        $FailedServices += $Service
    }
    
    Pop-Location
    Write-Host ""
}

Write-Host "╔═══════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  RESUMEN DE COMPILACIÓN                                       ║" -ForegroundColor Cyan
Write-Host "╠═══════════════════════════════════════════════════════════════╣" -ForegroundColor Cyan

if ($FailedServices.Count -eq 0) {
    Write-Host "║  ✓ Todos los servicios compilados exitosamente               ║" -ForegroundColor Green
    Write-Host "║  Próximo paso: .\run-all.ps1                                 ║" -ForegroundColor Green
} else {
    Write-Host "║  ✗ Servicios que fallaron:                                   ║" -ForegroundColor Red
    $FailedServices | ForEach-Object { Write-Host "║    • $_" -ForegroundColor Red }
}

Write-Host "╚═══════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""
