# Optimizaciones para t3.micro - Resumen

## ✅ Cambios Realizados para t3.micro

He optimizado la configuración del proyecto para ejecutarse en una instancia **t3.micro** (1GB RAM).

### 📦 Nuevos Archivos Creados

**Docker Compose:**
- `docker-compose-t3micro.yml` - Versión optimizada (PostgreSQL local)
- `docker-compose-t3micro-rds.yml` - Versión con AWS RDS (sin PostgreSQL local)

**Scripts:**
- `start-ec2-micro.sh` - Iniciar servicios en t3.micro
- `stop-ec2-micro.sh` - Detener servicios
- `monitor-ec2-micro.sh` - Monitorear RAM/CPU en tiempo real

**Documentación:**
- `T3MICRO-GUIA.md` - Guía completa para t3.micro

**Configuración:**
- `.env.ec2.example` - Actualizado con valores para t3.micro

---

## 🎯 Estrategia para t3.micro (1GB RAM)

### Lo que cabe:
```
PostgreSQL:    ~250MB
API Gateway:   ~128MB
ms-clientes:   ~128MB
ms-ecommerce:  ~96MB
ms-inventario: ~96MB
─────────────────────
TOTAL:         ~700MB ✓
```

### Lo que NO cabe:
```
✗ ms-ventas    (comentado - opcional)
✗ ms-financiero (comentado - opcional)
```

---

## 🚀 Cómo Usar en t3.micro

### Opción A: Con PostgreSQL Local (docker-compose-t3micro.yml)

```bash
# Preparar
cp .env.ec2.example .env.ec2
nano .env.ec2  # Editar valores

# Iniciar
chmod +x start-ec2-micro.sh
./start-ec2-micro.sh

# Monitorear
./monitor-ec2-micro.sh
```

### Opción B: Con AWS RDS (Recomendado para t3.micro)

```bash
# 1. Crear base de datos en AWS RDS (db.t3.micro gratuito)
# 2. Actualizar .env.ec2:
echo "RDS_ENDPOINT=tu-rds-endpoint.amazonaws.com" >> .env.ec2

# 3. Iniciar
docker-compose -f docker-compose-t3micro-rds.yml up -d

# Ventajas:
# ✓ Libera 250MB de RAM
# ✓ Backup automático
# ✓ Mejor seguridad
```

---

## 📊 Optimizaciones Implementadas

| Aspecto | Valor | Beneficio |
|---------|-------|----------|
| **Logging** | JSON-file (5MB max) | Ahorra espacio en disco |
| **JVM Memory** | 48-96MB por app | Cabe en 1GB |
| **Conexiones BD** | Max 5 | Menor consumo RAM |
| **Health Check** | 45s interval | Menos overhead |
| **CPU** | 0.3-0.4 burstable | Dentro de t3.micro |

---

## 🔧 Ajustes de Memoria JVM

En `.env.ec2`:
```bash
# Para t3.micro (128MB máximo por servicio)
JAVA_OPTS=-Xms48m -Xmx96m

# Para t3.small (512MB)
JAVA_OPTS=-Xms128m -Xmx256m

# Para t3.medium (1GB)
JAVA_OPTS=-Xms256m -Xmx512m
```

---

## ⚠️ Limitaciones de t3.micro

❌ **No puede correr:**
- Todos los 6 servicios simultáneamente
- Servicios de ventas y financiero sin quitar otros
- Logging en CloudWatch (sin configuración de créditos)

✅ **Puede correr:**
- Gateway + Clientes + Ecommerce + Inventario
- CRM y pedidos online
- Desarrollo y testing

---

## 📈 Plan de Upgradeo

```
t3.micro  (1GB)  → Desarrollo
    ↓
t3.small  (2GB)  → Todos los servicios
    ↓
t3.medium (4GB)  → Producción
```

---

## 💾 Uso de Disco

Con `docker-compose-t3micro.yml`:
```
Imágenes Docker: ~2.5GB
PostgreSQL data: Variable (inicialmente 100MB)
Logs:            ~50MB (5MB por archivo, max 2)
─────────────────────
TOTAL:           ~2.6GB de 10GB ✓ OK
```

---

## 🎯 Checklist Final

- [ ] Usar `docker-compose-t3micro.yml` (NO el original)
- [ ] Actualizar `.env.ec2` con memoria mínima
- [ ] Ejecutar `start-ec2-micro.sh`
- [ ] Verificar RAM: `./monitor-ec2-micro.sh` (debe estar < 700MB)
- [ ] Health check: `curl localhost:9090/actuator/health`
- [ ] Si falta RAM: upgradea a t3.small o usa RDS

---

## 📞 Comandos Rápidos para t3.micro

```bash
# Ver estado
./monitor-ec2-micro.sh

# Ver logs
docker-compose -f docker-compose-t3micro.yml logs -f api-gateway

# Parar todo
./stop-ec2-micro.sh

# Limpiar disco (CUIDADO - borra imágenes locales)
docker system prune -a

# Usar bash en contenedor
docker exec -it ms-clientes /bin/sh
```

---

**Ya está todo listo para t3.micro. Usa `start-ec2-micro.sh` para iniciar! 🚀**
