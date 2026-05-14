#!/bin/bash

# ============================================================
# Script para iniciar servicios en t3.micro de AWS
# Optimizado para 1GB RAM
# ============================================================

set -e

echo "=========================================="
echo "Iniciando servicios en t3.micro"
echo "=========================================="

# Crear red Docker si no existe
echo "Creando red Docker..."
docker network create cordillera-net || true

# Cargar variables de entorno
if [ -f .env.ec2 ]; then
    export $(cat .env.ec2 | xargs)
    echo "✓ Variables cargadas desde .env.ec2"
else
    echo "⚠ .env.ec2 no encontrado, usando valores por defecto"
fi

# Pre-pull de imágenes base
echo "Descargando imágenes base..."
docker pull postgres:16-alpine || true
docker pull eclipse-temurin:17.0.8_7-jre-alpine || true

# Build de imágenes
echo "Compilando microservicios (esto toma 10-15 minutos)..."
docker-compose -f docker-compose-t3micro.yml build

# Iniciar servicios
echo "Iniciando contenedores..."
docker-compose -f docker-compose-t3micro.yml up -d

# Esperar a que los servicios estén listos
echo "Esperando a que los servicios estén listos..."
sleep 40

# Mostrar estado
echo ""
echo "=========================================="
echo "Estado de los servicios:"
echo "=========================================="
docker ps

echo ""
echo "Uso de memoria (debe ser < 1GB):"
echo "=========================================="
docker stats --no-stream

echo ""
echo "=========================================="
echo "✓ Servicios iniciados correctamente"
echo "=========================================="
echo ""
echo "URLs de acceso:"
echo "  - API Gateway:      http://localhost:9090"
echo "  - Health Check:     http://localhost:9090/actuator/health"
echo "  - ms-clientes:      http://localhost:9095"
echo "  - ms-ecommerce:     http://localhost:9092"
echo "  - ms-inventario:    http://localhost:9093"
echo "  - PostgreSQL:       localhost:5432"
echo ""
echo "Para ver logs:"
echo "  docker-compose -f docker-compose-t3micro.yml logs -f [servicio]"
echo ""
echo "Para ver uso de RAM:"
echo "  docker stats"
echo ""
echo "Para detener:"
echo "  ./stop-ec2-micro.sh"
echo ""
