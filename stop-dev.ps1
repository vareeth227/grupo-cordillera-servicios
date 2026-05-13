# ============================================================
# Script para detener las bases de datos Docker
# Los microservicios se cierran cerrando sus ventanas PowerShell
# ============================================================

$baseDir = "d:\user\Desktop\Fullstack III"

Write-Host ""
Write-Host "Deteniendo bases de datos..." -ForegroundColor Yellow
Set-Location $baseDir
docker-compose -f docker-compose-db.yml down

Write-Host ""
Write-Host "Bases de datos detenidas." -ForegroundColor Green
Write-Host "Recuerda cerrar tambien las ventanas de los microservicios." -ForegroundColor Gray
Write-Host ""
Start-Sleep -Seconds 3
