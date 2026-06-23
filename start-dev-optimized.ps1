# start-dev-optimized.ps1
# Script para iniciar el entorno de desarrollo optimizado

# Detectar directorio automáticamente
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

Write-Host "Directorio de trabajo: $scriptDir"

# Validar que Docker está instalado
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Error "Docker no está instalado o no está en el PATH."
    exit 1
}

Write-Host "Docker está instalado."

# Levantar docker-compose-db.yml
Write-Host "Levantando bases de datos..."
docker-compose -f docker-compose-db.yml up -d

# Esperar a que BD estén listas (TCP health checks en puertos 5432-5436)
$ports = 5432, 5433, 5434, 5435, 5436
foreach ($port in $ports) {
    Write-Host "Esperando puerto $port..."
    $attempts = 0
    while ($attempts -lt 30) {
        try {
            $tcpClient = New-Object System.Net.Sockets.TcpClient
            $tcpClient.Connect("localhost", $port)
            $tcpClient.Close()
            Write-Host "Puerto $port listo."
            break
        } catch {
            Start-Sleep -Seconds 2
            $attempts++
        }
    }
    if ($attempts -eq 30) {
        Write-Error "Puerto $port no está disponible después de 60 segundos."
        exit 1
    }
}

Write-Host "Todas las bases de datos están listas."

# Levantar docker-compose-services.yml
Write-Host "Levantando servicios..."
docker-compose -f docker-compose-services.yml up -d

# Mostrar URLs de acceso
Write-Host "Entorno de desarrollo iniciado."
Write-Host "Frontend: http://localhost:3000"
Write-Host "API Gateway: http://localhost:8080"