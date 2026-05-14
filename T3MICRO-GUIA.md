# Guía de Configuración para t3.micro - Grupo Cordillera

## ⚠️ Limitaciones de t3.micro

**Hardware:**
- 1GB RAM total
- 2 vCPU (burstable, pueden bajar a 0.1 vCPU en inactividad)
- ~10GB almacenamiento

**Lo que esto significa:**
```
Memoria disponible: 1GB
Menos SO (~100MB): 900MB disponible

Consumo aproximado:
- PostgreSQL:     ~250MB
- API Gateway:    ~128MB
- ms-clientes:    ~128MB
- ms-ecommerce:   ~96MB
- ms-inventario:  ~96MB
─────────────────────────
TOTAL:            ~700MB (LÍMITE CRÍTICO)

Margin: ~200MB (MUY AJUSTADO)
```

**Resultado: No puedes ejecutar TODOS los servicios simultáneamente.**

---

## 🎯 Estrategia para t3.micro

### Opción A: Servicios Críticos Solo (RECOMENDADO)
Ejecutar solo los servicios esenciales:
```
✓ PostgreSQL
✓ API Gateway (enrutador)
✓ ms-clientes (CRM, esencial)
✓ ms-ecommerce (core business)
✓ ms-inventario (dependencia de ecommerce)
✗ ms-ventas (comentado)
✗ ms-financiero (comentado)
```

**Uso RAM: ~700MB** ✓ Funciona

### Opción B: Servicios en Rotación
```
Ejecutar solo los que necesitas en ese momento:

Turno 1:
  - PostgreSQL
  - API Gateway
  - ms-clientes
  (Usar solo este, ~550MB)

Turno 2:
  - PostgreSQL
  - API Gateway
  - ms-ecommerce
  - ms-inventario
  (Cuando necesitas ecommerce, ~700MB)
```

### Opción C: Usar RDS (MEJOR SOLUCIÓN)
```
En lugar de PostgreSQL local:
✓ Usar AWS RDS PostgreSQL (db.t3.micro es gratuito)
✓ Libera 250MB RAM en la instancia
✓ Mejor disponibilidad (backup automático)
✓ Mejor performance

Usa docker-compose-t3micro-rds.yml
```

---

## 📋 Paso a Paso para t3.micro

### 1. Clonar y configurar

```bash
# SSH a EC2
ssh -i "tu-clave.pem" ec2-user@tu-instancia

# Clonar
git clone https://github.com/vareeth227/grupo-cordillera-servicios.git
cd grupo-cordillera-servicios

# Dar permisos
chmod +x *.sh
```

### 2. Crear .env.ec2 para t3.micro

```bash
cp .env.ec2.example .env.ec2
nano .env.ec2
```

Editar con valores mínimos:
```bash
# Database
POSTGRES_USER=cordillera
POSTGRES_PASSWORD=TU_CONTRASEÑA_FUERTE

# JWT
JWT_SECRET=tu-jwt-secret-2024

# JVM memory (IMPORTANTE para t3.micro)
JAVA_OPTS=-Xms48m -Xmx96m

# Sin CloudWatch (ahorra espacio en logs)
# AWS_ACCESS_KEY_ID=
# AWS_SECRET_ACCESS_KEY=
```

### 3. Compilar imágenes (PRIMERA VEZ)

```bash
# Esto toma 10-15 minutos
docker-compose -f docker-compose-t3micro.yml build

# Ver imágenes creadas
docker images
```

### 4. Iniciar servicios

```bash
# Opción A: Script automático
chmod +x start-ec2.sh
./start-ec2.sh

# Opción B: Manual
docker-compose -f docker-compose-t3micro.yml up -d

# Ver estado
docker ps
```

### 5. Verificar estado

```bash
# Uso de memoria en tiempo real
docker stats --no-stream

# Health check
curl http://localhost:9090/actuator/health

# Ver logs
docker-compose -f docker-compose-t3micro.yml logs -f api-gateway
```

---

## 🔧 Optimizaciones para t3.micro

### JVM Memory Tuning

Cada microservicio tiene JAVA_TOOL_OPTIONS optimizado:

```
api-gateway:   -Xms64m -Xmx128m   (128MB máximo)
ms-clientes:   -Xms64m -Xmx128m   (128MB máximo)
ms-ecommerce:  -Xms48m -Xmx96m    (96MB máximo)
ms-inventario: -Xms48m -Xmx96m    (96MB máximo)
```

Si necesitas más RAM para un servicio:
```yaml
environment:
  JAVA_TOOL_OPTIONS: "-Xms64m -Xmx192m"  # Aumentar
```

### Logging Optimizado

En lugar de CloudWatch, usa JSON-file logs:
```yaml
logging:
  driver: "json-file"
  options:
    max-size: "5m"    # Máximo 5MB por archivo
    max-file: "2"     # Solo 2 archivos (10MB total)
```

Esto ahorra espacio en disco (~1GB disponible).

### Health Checks Ajustados

Para t3.micro más lento:
```yaml
healthcheck:
  interval: 45s          # Menos frecuente
  start_period: 60s      # Más tiempo de startup
  retries: 2             # Menos reintentos
```

---

## 📊 Monitoreo en t3.micro

### Ver uso de RAM en tiempo real
```bash
docker stats --no-stream

# Salida esperada:
# CONTAINER  CPU   MEM USAGE / LIMIT
# postgres   2.1%  248M / 256M
# api-gateway 1.5% 112M / 256M
# ms-clientes 1.2% 98M / 256M
# TOTAL:           458M / 1G  ✓ OK
```

### Si se queda sin memoria

```bash
# 1. Ver qué consume más
docker stats --no-stream

# 2. Reducir límites en docker-compose-t3micro.yml
# Ej: memory: 192M → 128M

# 3. Reiniciar
docker-compose down
docker-compose -f docker-compose-t3micro.yml up -d

# 4. Si aún no cabe, descomenta ms-ventas y ms-financiero
```

---

## 🚀 Scripts para t3.micro

### start-ec2-micro.sh

```bash
#!/bin/bash
docker network create cordillera-net || true
docker-compose -f docker-compose-t3micro.yml up -d
sleep 30
docker stats --no-stream
```

### stop-ec2-micro.sh

```bash
#!/bin/bash
docker-compose -f docker-compose-t3micro.yml down
```

Uso:
```bash
chmod +x start-ec2-micro.sh stop-ec2-micro.sh
./start-ec2-micro.sh
./stop-ec2-micro.sh
```

---

## ⚠️ Problemas Comunes en t3.micro

### "No space left on device"
```bash
# Limpiar Docker
docker system prune -a --volumes

# Ver espacio
df -h
```

### Contenedores se reinician constantemente
```bash
# Ver logs
docker logs --tail 50 [servicio]

# Probable causa: OOM (Out of Memory)
# Solución: Reducir memory limit en docker-compose-t3micro.yml
```

### Lentísimo (bursting)
```bash
# t3.micro solo tiene CPU burstable
# Es lento cuando accede mucho a disco

# Soluciones:
# 1. Optimizar índices de base de datos
# 2. Agregar caché (Redis)
# 3. Upgradear a t3.small o t3.medium
```

### Base de datos no conecta
```bash
# Verificar PostgreSQL está up
docker ps | grep postgres

# Ver logs de PostgreSQL
docker logs postgres

# Reiniciar BD
docker-compose -f docker-compose-t3micro.yml restart postgres
```

---

## 💡 Alternativas Mejores que t3.micro

### Para Desarrollo
```
t3.small (2GB RAM)  →  Cost: $0.018/hora ($13/mes)
- Cómodo para todos los servicios
- Sin problemas de memoria
```

### Para Producción
```
t3.medium (4GB RAM) →  Cost: $0.0416/hora ($30/mes)
- Recomendado mínimo para producción
- Espacio para crecer
```

### Opción Híbrida (Recomendada para t3.micro)
```
t3.micro + AWS RDS PostgreSQL (db.t3.micro gratis)
- Libera 250MB de RAM
- Mejor backup automático
- Mejor seguridad
- Costo = $0 (dentro de free tier si aplicas)
```

---

## 📈 Plan de Crecimiento desde t3.micro

### Fase 1: Desarrollo (t3.micro)
```
✓ ms-clientes
✓ API Gateway
✓ PostgreSQL local
```

### Fase 2: Testing (t3.small)
```
✓ Todos los servicios
✓ PostgreSQL local
```

### Fase 3: Producción (t3.medium + RDS)
```
✓ Todos los servicios + API Gateway
✓ PostgreSQL en AWS RDS
✓ CloudWatch logs
✓ Auto-scaling
```

---

## 🎯 Checklist para t3.micro

- [ ] Instancia t3.micro en EC2
- [ ] Docker instalado
- [ ] Repositorio clonado
- [ ] `.env.ec2` creado y configurado
- [ ] `docker-compose-t3micro.yml` seleccionado
- [ ] Primera compilación: `docker-compose -f docker-compose-t3micro.yml build`
- [ ] Servicios iniciados: `./start-ec2.sh`
- [ ] Health check pasa: `curl localhost:9090/actuator/health`
- [ ] RAM bajo control: `docker stats`

---

## 📞 Comandos Útiles en t3.micro

```bash
# Ver estado
docker ps

# Ver RAM/CPU
docker stats --no-stream

# Ver logs
docker logs -f [servicio]

# Reiniciar un servicio
docker-compose -f docker-compose-t3micro.yml restart ms-clientes

# Parar todo
docker-compose -f docker-compose-t3micro.yml down

# Limpiar espacio
docker system prune -a

# Entrar a base de datos
docker exec -it postgres psql -U cordillera -d db_clientes
```

---

## 🏁 Conclusión

**t3.micro funciona pero con limitaciones:**

✓ Puedes correr los servicios críticos
✓ Es gratis en free tier
✓ Válido para desarrollo/testing

✗ NO es recomendable para producción
✗ Muy lento con CPU burstable
✗ Sin margen de error

**Recomendación:** Usa t3.micro para desarrollo. Cuando llegues a producción, upgradea a t3.small o t3.medium.
