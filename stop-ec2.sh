#!/bin/bash

# ============================================================
# Script para detener servicios en EC2
# ============================================================

set -e

echo "=========================================="
echo "Deteniendo servicios en EC2"
echo "=========================================="

# Detener contenedores
docker-compose -f docker-compose-ec2.yml down

echo "✓ Servicios detenidos correctamente"
