# Script para ejecutar todos los microservicios en paralelo
# Cada uno corre como un proceso Java independiente

Write-Host "`n╔═══════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  Ejecutando microservicios en paralelo                        ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

# Configuración de cada servicio
$Services = @(
    @{
        Name = "ms-clientes"
        Port = 9095
        Jar  = "target/ms-clientes-0.0.1-SNAPSHOT.jar"
        Args = ""
    },
    @{
        Name = "ms-ventas"
        Port = 9091
        Jar  = "target/ms-ventas-0.0.1-SNAPSHOT.jar"
        Args = ""
    },
    @{
        Name = "ms-inventario"
        Port = 9093
        Jar  = "target/ms-inventario-0.0.1-SNAPSHOT.jar"
        Args = ""
    },
    @{
        Name = "ms-ecommerce"
        Port = 9092
        Jar  = "target/ms-ecommerce-0.0.1-SNAPSHOT.jar"
        Args = "-Dms.clientes.url=http://localhost:9095 -Dms.inventario.url=http://localhost:9093"
    },
    @{
        Name = "ms-financiero"
        Port = 9094
        Jar  = "target/ms-financiero-0.0.1-SNAPSHOT.jar"
        Args = ""
    },
    @{
        Name = "api-gateway"
        Port = 9090
        Jar  = "target/api-gateway-0.0.1-SNAPSHOT.jar"
        Args = ""
    }
)

# Verificar que las bases de datos están disponibles
Write-Host "🔍 Verificando conexión a bases de datos..." -ForegroundColor Yellow

$MaxRetries = 10
$Retries = 0

while ($Retries -lt $MaxRetries) {
    try {
        $Response = curl -s -m 2 "http://localhost:5432" -ErrorAction SilentlyContinue
        Write-Host "✓ Bases de datos disponibles" -ForegroundColor Green
        break
    } catch {
        $Retries++
        if ($Retries -lt $MaxRetries) {
            Write-Host "  ⏳ Intento $Retries/$MaxRetries - Esperando bases de datos..." -ForegroundColor Gray
            Start-Sleep -Seconds 3
        }
    }
}

if ($Retries -ge $MaxRetries) {
    Write-Host "✗ No se pudo conectar a las bases de datos" -ForegroundColor Red
    Write-Host "  Asegúrate de ejecutar: docker-compose -f docker-compose-db-only.yml up -d" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Crear directorio de logs
$LogDir = Join-Path (Get-Location) "logs"
if (-not (Test-Path $LogDir)) {
    New-Item -ItemType Directory -Path $LogDir | Out-Null
}

Write-Host "📋 Iniciando servicios..." -ForegroundColor Yellow
Write-Host ""

# Iniciar cada servicio en background
$RunningProcesses = @{}

foreach ($Service in $Services) {
    $ServiceName = $Service.Name
    $JarPath = Join-Path $ServiceName $Service.Jar
    $Port = $Service.Port
    
    if (-not (Test-Path $JarPath)) {
        Write-Host "✗ No encontrado: $JarPath" -ForegroundColor Red
        Write-Host "  Ejecuta primero: .\build-all.ps1" -ForegroundColor Yellow
        continue
    }
    
    Write-Host "▶️  $ServiceName (puerto $Port)" -ForegroundColor Cyan
    
    $LogFile = Join-Path $LogDir "$ServiceName.log"
    
    # Iniciar servicio en background
    $Process = Start-Process -FilePath "java" `
        -ArgumentList "-Dserver.port=$Port $($Service.Args) -jar `"$JarPath`"" `
        -RedirectStandardOutput $LogFile `
        -RedirectStandardError $LogFile `
        -PassThru `
        -WindowStyle Hidden
    
    $RunningProcesses[$ServiceName] = $Process.Id
    Write-Host "   PID: $($Process.Id) | Logs: $LogFile" -ForegroundColor Gray
    
    # Esperar 5 segundos entre inicios
    Start-Sleep -Seconds 5
}

Write-Host ""
Write-Host "⏳ Esperando a que los servicios inicien (60 segundos)..." -ForegroundColor Yellow

Start-Sleep -Seconds 60

Write-Host ""
Write-Host "╔═══════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║  ✅ SERVICIOS EJECUTÁNDOSE                                    ║" -ForegroundColor Green
Write-Host "╠═══════════════════════════════════════════════════════════════╣" -ForegroundColor Green

foreach ($Service in $Services) {
    $Port = $Service.Port
    try {
        $Response = curl -s -m 1 "http://localhost:$Port/actuator/health" -ErrorAction SilentlyContinue
        if ($Response -like '*"status":"UP"*' -or $Response -like "*UP*") {
            Write-Host "║ ✓ $($Service.Name.PadRight(20)) http://localhost:$Port" -ForegroundColor Green
        } else {
            Write-Host "║ ⏳ $($Service.Name.PadRight(20)) (iniciándose...)" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "║ ⏳ $($Service.Name.PadRight(20)) (iniciándose...)" -ForegroundColor Yellow
    }
}

Write-Host "║                                                               ║" -ForegroundColor Green
Write-Host "║ 📁 Logs en: ./logs/                                           ║" -ForegroundColor Green
Write-Host "║                                                               ║" -ForegroundColor Green
Write-Host "║ Comandos útiles:                                              ║" -ForegroundColor Green
Write-Host "║ • Ver logs: Get-Content logs/ms-clientes.log -Tail 50        ║" -ForegroundColor Green
Write-Host "║ • Matar servicio: Stop-Process <PID>                          ║" -ForegroundColor Green
Write-Host "║ • Detener BDs: docker-compose -f docker-compose-db-only.yml down" -ForegroundColor Green
Write-Host "║                                                               ║" -ForegroundColor Green
Write-Host "╚═══════════════════════════════════════════════════════════════╝" -ForegroundColor Green

Write-Host ""
Write-Host "PIDs en ejecución:" -ForegroundColor Cyan
$RunningProcesses.GetEnumerator() | ForEach-Object {
    Write-Host "  $($_.Key): $($_.Value)" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Presiona Ctrl+C para detener los servicios (o cierra este PowerShell)" -ForegroundColor Yellow
Write-Host ""

# Mantener el script ejecutándose
while ($true) {
    Start-Sleep -Seconds 10
    
    # Verificar si algún proceso falló
    foreach ($Service in $Services) {
        $Pid = $RunningProcesses[$Service.Name]
        if ($Pid) {
            $Process = Get-Process -Id $Pid -ErrorAction SilentlyContinue
            if (-not $Process) {
                Write-Host "⚠️  $($Service.Name) se detuvo inesperadamente" -ForegroundColor Red
            }
        }
    }
}
