# Script para compilar y ejecutar el proyecto sin Docker Desktop
# Solo usa Java, Maven y PostgreSQL en contenedor (muy ligero)

$MavenPath = "C:\Program Files\NetBeans-23\netbeans\java\maven\bin\mvn.cmd"

if (-not (Test-Path $MavenPath)) {
    Write-Host "❌ Maven no encontrado en: $MavenPath" -ForegroundColor Red
    exit 1
}

Write-Host "`n╔═══════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  Grupo Cordillera - Setup sin Docker Desktop                 ║" -ForegroundColor Cyan
Write-Host "║  (Solo Java + Maven + PostgreSQL)                            ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

# Crear alias para Maven
Set-Alias mvn $MavenPath -Scope Global

Write-Host "✓ Maven configurado: $MavenPath" -ForegroundColor Green

Write-Host "`nPasos para ejecutar el proyecto:" -ForegroundColor Yellow
Write-Host "1. Ejecuta: docker-compose -f docker-compose-db-only.yml up -d" -ForegroundColor Cyan
Write-Host "2. Espera 30 segundos" -ForegroundColor Cyan
Write-Host "3. Ejecuta: .\build-all.ps1" -ForegroundColor Cyan
Write-Host "4. Ejecuta: .\run-all.ps1" -ForegroundColor Cyan
Write-Host ""
