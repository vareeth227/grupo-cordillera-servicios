#!/bin/bash

# ============================================================
# Script para detener servicios en t3.micro
# ============================================================

set -e

echo "=========================================="
echo "Deteniendo servicios en t3.micro"
echo "=========================================="

docker-compose -f docker-compose-t3micro.yml down

echo "✓ Servicios detenidos correctamente"
echo ""
echo "Para limpiar espacio en disco:"
echo "  docker system prune -a"
echo ""
