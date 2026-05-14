#!/bin/bash

# ============================================================
# Script de monitoreo para servicios en EC2
# Verifica salud, uso de recursos y logs de errores
# ============================================================

set -e

COMPOSE_FILE="docker-compose-ec2.yml"
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=========================================="
echo "Monitor de Servicios - Grupo Cordillera"
echo "=========================================="
echo ""

# Función para mostrar estado de un servicio
check_service() {
    local service=$1
    local port=$2
    
    if docker ps | grep -q "$service"; then
        echo -ne "${GREEN}✓${NC} $service "
        
        # Verificar health check
        if docker inspect --format='{{.State.Health.Status}}' "$service" 2>/dev/null | grep -q "healthy"; then
            echo -ne "(${GREEN}healthy${NC}) "
        else
            echo -ne "(${YELLOW}unknown${NC}) "
        fi
        
        # Usar CPU y Memoria
        local stats=$(docker stats --no-stream --format "{{.CPUPerc}},{{.MemUsage}}" "$service" 2>/dev/null)
        echo "CPU: ${stats%,*} MEM: ${stats#*,}"
    else
        echo -e "${RED}✗${NC} $service (${RED}NOT RUNNING${NC})"
    fi
}

# Verificar estado de contenedores
echo "Estado de Servicios:"
echo "────────────────────"
check_service "postgres" "5432"
check_service "api-gateway" "9090"
check_service "ms-clientes" "9095"
check_service "ms-ecommerce" "9092"
check_service "ms-inventario" "9093"
check_service "ms-ventas" "9091"
check_service "ms-financiero" "9094"

echo ""
echo "Uso Total de Recursos:"
echo "──────────────────────"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}"

echo ""
echo "Errores Recientes en Logs:"
echo "─────────────────────────"
for service in postgres api-gateway ms-clientes ms-ecommerce ms-inventario ms-ventas ms-financiero; do
    errors=$(docker logs --tail 50 "$service" 2>&1 | grep -i "error\|exception\|fatal" | wc -l)
    if [ $errors -gt 0 ]; then
        echo -e "${RED}!${NC} $service tiene $errors errores en los últimos logs"
        docker logs --tail 5 "$service" 2>&1 | grep -i "error\|exception\|fatal" | head -1
    fi
done

echo ""
echo "Almacenamiento:"
echo "───────────────"
docker system df

echo ""
echo "Para ver logs detallados:"
echo "  docker-compose -f $COMPOSE_FILE logs -f [servicio]"
echo ""
echo "Para ver estadísticas en tiempo real:"
echo "  docker stats"
echo ""
