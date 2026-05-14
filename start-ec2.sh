#!/bin/bash

# ============================================================
# Script para iniciar servicios en EC2
# ============================================================

set -e  # Exit si hay error

echo "=========================================="
echo "Iniciando servicios en EC2"
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

# Pre-pull de imágenes base para ahorrar tiempo
echo "Descargando imágenes base..."
docker pull maven:3.9.6-eclipse-temurin-17 || true
docker pull eclipse-temurin:17.0.8_7-jre-alpine || true
docker pull postgres:16-alpine || true

# Build de imágenes (sin caché para builds frescos)
echo "Compilando microservicios..."
docker-compose -f docker-compose-ec2.yml build --no-cache

# Iniciar servicios
echo "Iniciando contenedores..."
docker-compose -f docker-compose-ec2.yml up -d

# Esperar a que los servicios estén listos
echo "Esperando a que los servicios estén listos..."
sleep 30

# Validar salud de los servicios
echo "Validando estado de los servicios..."
for service in postgres api-gateway ms-clientes ms-ecommerce ms-inventario ms-ventas ms-financiero; do
    if docker ps | grep -q $service; then
        echo "✓ $service está corriendo"
    else
        echo "✗ PROBLEMA: $service no está corriendo"
        exit 1
    fi
done

# Mostrar URLs
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
echo "  - ms-ventas:        http://localhost:9091"
echo "  - ms-financiero:    http://localhost:9094"
echo "  - PostgreSQL:       localhost:5432"
echo ""
echo "Para ver logs:"
echo "  docker-compose -f docker-compose-ec2.yml logs -f [servicio]"
echo ""
echo "Para detener:"
echo "  docker-compose -f docker-compose-ec2.yml down"
echo ""
