#!/bin/bash

# ============================================================
# Script de monitoreo para t3.micro
# Muestra uso de RAM, CPU y errores
# ============================================================

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "=========================================="
echo "Monitor de t3.micro - Grupo Cordillera"
echo "=========================================="
echo ""

# Verificar docker
if ! command -v docker &> /dev/null; then
    echo "Docker no está instalado"
    exit 1
fi

# Header
echo "Consumo de Recursos:"
echo "──────────────────────────────────────────"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}"

echo ""
echo "Contenedores corriendo:"
echo "──────────────────────────────────────────"
docker ps --format "table {{.Names}}\t{{.Status}}"

echo ""
echo "Espacio en disco:"
echo "──────────────────────────────────────────"
df -h | grep -E "^/dev|Filesystem"

echo ""
echo "Errores recientes:"
echo "──────────────────────────────────────────"
has_errors=0
for service in postgres api-gateway ms-clientes ms-ecommerce ms-inventario; do
    errors=$(docker logs --tail 20 "$service" 2>&1 | grep -i "error\|exception\|fatal" | wc -l)
    if [ $errors -gt 0 ]; then
        echo -e "${RED}!${NC} $service tiene $errors errores"
        docker logs --tail 3 "$service" 2>&1 | grep -i "error\|exception\|fatal" | head -1
        has_errors=1
    fi
done

if [ $has_errors -eq 0 ]; then
    echo -e "${GREEN}✓${NC} Sin errores detectados"
fi

echo ""
echo "Health status:"
echo "──────────────────────────────────────────"
for service in postgres api-gateway ms-clientes ms-ecommerce ms-inventario; do
    if docker ps | grep -q "^$service" || docker ps | grep -q " $service "; then
        status=$(docker inspect --format='{{.State.Health.Status}}' "$service" 2>/dev/null)
        if [ -z "$status" ]; then
            status="unknown"
        fi
        
        if [ "$status" == "healthy" ]; then
            echo -e "${GREEN}✓${NC} $service: $status"
        else
            echo -e "${YELLOW}~${NC} $service: $status"
        fi
    else
        echo -e "${RED}✗${NC} $service: not running"
    fi
done

echo ""
echo "Recomendaciones:"
echo "──────────────────────────────────────────"

# Calcular memoria total
mem_usage=$(docker stats --no-stream --format "{{.MemUsage}}" | grep -oP '[0-9.]+(?=M)' | awk '{s+=$1} END {print s}')
echo "Memoria total usada: ${mem_usage}MB / 1000MB"

if (( $(echo "$mem_usage > 800" | bc -l) )); then
    echo -e "${RED}CRÍTICO:${NC} Casi sin memoria. Considera:"
    echo "  1. docker system prune (libera espacio)"
    echo "  2. Reducir limits en docker-compose-t3micro.yml"
    echo "  3. Descomenta ms-ventas y ms-financiero"
    echo "  4. Upgradea a t3.small (2GB)"
elif (( $(echo "$mem_usage > 650" )); then
    echo -e "${YELLOW}ADVERTENCIA:${NC} Uso alto de memoria"
    echo "  - Monitor constante recomendado"
    echo "  - Evita picos de tráfico"
fi

echo ""
