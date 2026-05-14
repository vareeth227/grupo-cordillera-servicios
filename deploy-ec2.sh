#!/bin/bash

# ============================================================
# Script de deploy para EC2 con Git
# Actualiza código y reinicia servicios
# ============================================================

set -e

PROJECT_DIR="/home/ec2-user/grupo-cordillera-servicios"
BRANCH="main"

echo "=========================================="
echo "Deploy de Grupo Cordillera"
echo "=========================================="

# Ir al directorio del proyecto
cd "$PROJECT_DIR"

# Mostrar versión actual
echo "Versión actual:"
git log -1 --oneline

# Actualizar código
echo ""
echo "Actualizando código..."
git fetch origin
git checkout $BRANCH
git pull origin $BRANCH

# Mostrar nueva versión
echo "Nueva versión:"
git log -1 --oneline

# Actualizar variables de entorno (sin sobrescribir)
if [ ! -f .env.ec2 ]; then
    cp .env.ec2.example .env.ec2
    echo "⚠ Nuevo .env.ec2 creado. EDITAR CON VALORES REALES antes de iniciar."
    exit 1
fi

# Hacer backup de volúmenes (opcional)
echo ""
echo "Haciendo backup de datos..."
mkdir -p backups
docker run --rm \
    -v postgres_data:/postgres \
    -v "$(pwd)/backups:/backup" \
    postgres:16-alpine \
    bash -c "pg_dump -U postgres postgres > /backup/db_backup_$(date +%Y%m%d_%H%M%S).sql" || true

# Detener servicios actuales
echo ""
echo "Deteniendo servicios..."
docker-compose -f docker-compose-ec2.yml down

# Iniciar nuevos servicios
echo ""
echo "Iniciando servicios..."
./start-ec2.sh

# Verificar deploy
echo ""
echo "Verificando deploy..."
sleep 20

# Health check
for i in {1..10}; do
    if curl -s http://localhost:9090/actuator/health | grep -q '"status":"UP"'; then
        echo "✓ Deploy exitoso"
        exit 0
    fi
    echo "  Intento $i de 10..."
    sleep 5
done

echo "✗ Deploy falló - Health check no pasó"
exit 1
