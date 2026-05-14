# Setup de Grupo Cordillera en EC2

## 1. Preparar instancia EC2 (Ubuntu 22.04 LTS)

### Requisitos mínimos
```bash
# t3.medium: 4GB RAM, 2 vCPU
# t3.large: 8GB RAM, 2 vCPU (recomendado para producción)
```

### Security Group
Abrir los siguientes puertos:
```
22 (SSH)        - para tu IP
80 (HTTP)       - público (redirige a 443)
443 (HTTPS)     - público
9090 (Gateway)  - solo desde tu IP/ALB
```

### Volumen EBS
Mínimo 50GB gp3 para almacenar imágenes y datos.

---

## 2. Instalar Docker y dependencias

```bash
# Actualizar sistema
sudo apt-get update && sudo apt-get upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Agregar usuario al grupo docker (sin sudo)
sudo usermod -aG docker $USER
newgrp docker

# Instalar utilidades
sudo apt-get install -y git curl wget htop

# Verificar instalación
docker --version
docker-compose --version
```

---

## 3. Clonar repositorio

```bash
# Ir a home
cd ~

# Clonar el proyecto
git clone https://github.com/vareeth227/grupo-cordillera-servicios.git
cd grupo-cordillera-servicios

# Ver rama actual
git branch -a
```

---

## 4. Configurar variables de entorno

```bash
# Crear .env.ec2
cp .env.ec2.example .env.ec2

# Editar con valores reales
nano .env.ec2
```

Valores clave a actualizar:
```
POSTGRES_USER=cordillera
POSTGRES_PASSWORD=tu_contraseña_fuerte_aqui
JWT_SECRET=tu_jwt_secret_aqui
CORS_ALLOWED_ORIGINS=https://tudominio.com
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=tu_access_key
AWS_SECRET_ACCESS_KEY=tu_secret_key
```

---

## 5. Dar permisos a scripts

```bash
chmod +x start-ec2.sh
chmod +x stop-ec2.sh
chmod +x monitor-ec2.sh
chmod +x deploy-ec2.sh
```

---

## 6. Primer startup

```bash
# Construir e iniciar
./start-ec2.sh

# Este proceso toma 5-10 minutos la primera vez
# Verás logs de compilación de Maven
```

---

## 7. Verificar que funciona

```bash
# Ver estado de contenedores
docker ps

# Health check
curl http://localhost:9090/actuator/health

# Ver logs
docker-compose -f docker-compose-ec2.yml logs -f api-gateway
```

---

## 8. Configurar SSL con Let's Encrypt (opcional pero recomendado)

```bash
# Instalar certbot
sudo apt-get install -y certbot python3-certbot-nginx

# Generar certificado (reemplazar dominio)
sudo certbot certonly --standalone -d api.yourdomain.com

# El certificado se guarda en:
# /etc/letsencrypt/live/api.yourdomain.com/

# Auto-renovación (cron job)
sudo certbot renew --dry-run
```

---

## 9. Configurar Nginx como reverse proxy (opcional)

```bash
# Instalar Nginx
sudo apt-get install -y nginx

# Copiar configuración
sudo cp nginx-cordillera.conf /etc/nginx/sites-available/cordillera

# Crear symlink
sudo ln -s /etc/nginx/sites-available/cordillera /etc/nginx/sites-enabled/

# Desactivar default
sudo rm /etc/nginx/sites-enabled/default

# Validar configuración
sudo nginx -t

# Iniciar Nginx
sudo systemctl start nginx
sudo systemctl enable nginx

# Ver logs
sudo tail -f /var/log/nginx/cordillera-error.log
```

---

## 10. Auto-startup del servicio (systemd)

```bash
# Copiar archivo de servicio
sudo cp cordillera-servicios.service /etc/systemd/system/

# Recargar daemon
sudo systemctl daemon-reload

# Habilitar auto-start
sudo systemctl enable cordillera-servicios

# Iniciar servicio
sudo systemctl start cordillera-servicios

# Ver estado
sudo systemctl status cordillera-servicios

# Ver logs
sudo journalctl -u cordillera-servicios -f
```

---

## 11. Monitoreo

### Ver estado en tiempo real
```bash
./monitor-ec2.sh
```

### Ver logs de un servicio
```bash
docker-compose -f docker-compose-ec2.yml logs -f ms-clientes
docker logs --tail 50 --follow ms-clientes
```

### Estadísticas de CPU/Memoria
```bash
docker stats
```

### Ver volumenes
```bash
docker volume ls
docker volume inspect postgres_data
```

---

## 12. Backup automático de base de datos

```bash
# Crear script de backup
cat > backup-db.sh << 'EOF'
#!/bin/bash
BACKUP_DIR="/home/ec2-user/backups"
mkdir -p $BACKUP_DIR
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

docker exec postgres \
  pg_dump -U cordillera -d db_clientes > $BACKUP_DIR/db_clientes_$TIMESTAMP.sql

# Mantener solo los últimos 7 días
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
EOF

chmod +x backup-db.sh

# Programar backup diario (cron)
crontab -e

# Agregar:
# 0 2 * * * /home/ec2-user/grupo-cordillera-servicios/backup-db.sh
```

---

## 13. Deploy de nuevas versiones

```bash
# Actualizar y reiniciar
./deploy-ec2.sh

# Esto hace:
# 1. git pull (actualiza código)
# 2. Hace backup de DB
# 3. docker-compose build (compilación)
# 4. Inicia nuevos servicios
# 5. Health check
```

---

## 14. Troubleshooting

### Contenedor se reinicia constantemente
```bash
# Ver logs
docker logs --tail 50 ms-clientes

# Problema común: memoria insuficiente
# Aumentar JAVA_OPTS en .env.ec2
```

### Base de datos no conecta
```bash
# Verificar que PostgreSQL esté up
docker ps | grep postgres

# Conectar a base de datos
docker exec -it postgres psql -U cordillera -d db_clientes
```

### Network connectivity issues
```bash
# Listar redes
docker network ls

# Inspeccionar red
docker network inspect cordillera-net

# Probar conectividad
docker exec api-gateway curl http://ms-clientes:9095/actuator/health
```

### Limpiar espacio
```bash
# Eliminar contenedores parados
docker container prune

# Eliminar imágenes sin usar
docker image prune

# Eliminar volúmenes sin usar
docker volume prune

# Limpieza completa (CUIDADO)
docker system prune -a
```

---

## 15. Monitoring con CloudWatch (AWS)

Si usas logging con CloudWatch:

```bash
# Instalar agent (opcional)
sudo apt-get install amazon-cloudwatch-agent

# Ver logs en AWS Console
# CloudWatch > Logs > /ecs/cordillera-*

# Crear alarmas para:
# - Errores en logs
# - Alto uso de CPU
# - Alto uso de memoria
# - Health check failures
```

---

## Comandos útiles del día a día

```bash
# Ver estado de servicios
./monitor-ec2.sh

# Parar servicios
./stop-ec2.sh

# Iniciar servicios
./start-ec2.sh

# Ver logs del gateway
docker logs -f api-gateway

# Reiniciar un servicio
docker-compose -f docker-compose-ec2.yml restart ms-clientes

# Entrar a un contenedor
docker exec -it ms-clientes /bin/sh

# Ver variables de entorno
docker exec ms-clientes env | grep SPRING

# Conectar a base de datos
docker exec -it postgres psql -U cordillera

# Ver tamaño de imágenes
docker images --format "table {{.Repository}}\t{{.Size}}"
```

---

## Checklist Final

- [ ] Instancia EC2 creada (t3.medium mínimo)
- [ ] Security Groups configurados
- [ ] Docker instalado
- [ ] Repositorio clonado
- [ ] `.env.ec2` configurado
- [ ] Primer `./start-ec2.sh` exitoso
- [ ] Health check pasando
- [ ] SSL/TLS configurado (si producción)
- [ ] Backups de DB configurados
- [ ] Monitoreo activo
- [ ] Plan de rollback documentado

---

## Soporte

Para issues:
1. Revisar logs: `./monitor-ec2.sh`
2. Ver detalles: `docker logs [servicio]`
3. Recrear servicio: `docker-compose -f docker-compose-ec2.yml up -d --force-recreate [servicio]`
4. Contactar equipo de DevOps/Backend
