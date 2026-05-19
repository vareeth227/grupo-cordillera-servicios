# 🚀 INSTRUCCIONES DE DESPLIEGUE

## ❓ ¿CORRE DESDE CUALQUIER PC?

**Respuesta**: ✅ **SÍ**, pero necesitas cambios según donde lo despliegues.

---

## 📍 ESCENARIO 1: DESARROLLO LOCAL (TODO EN MISMA PC)

### Cambios necesarios: **NINGUNO**

Solo ejecuta:
```powershell
# Terminal 1
cd api-gateway && mvn spring-boot:run

# Terminal 2
cd ms-ventas && mvn spring-boot:run

# Terminal 3
cd ms-clientes && mvn spring-boot:run
```

**Acceso frontend**:
```
http://localhost:9090
```

**Archivo .env del frontend**:
```env
VITE_API_URL=http://localhost:9090
```

---

## 🌐 ESCENARIO 2: RED LOCAL (Backend en PC-B, Frontend en PC-A)

### Cambios necesarios:

**1. Obtener la IP de PC-B** (donde corre el backend):
```powershell
ipconfig

# Busca "Dirección IPv4" (ejemplo: 192.168.1.135)
```

**2. Actualizar en PC-A (.env del frontend)**:
```env
VITE_API_URL=http://192.168.1.135:9090
```

**3. En PC-B, ejecutar los servicios** (sin cambios):
```powershell
cd api-gateway && mvn spring-boot:run
cd ms-ventas && mvn spring-boot:run
cd ms-clientes && mvn spring-boot:run
```

**CORS en backend**: ✅ Ya está configurado para aceptar `http://192.168.1.*:*`

---

## ☁️ ESCENARIO 3: PRODUCCIÓN EN EC2 / SERVIDOR REMOTO

### Cambios necesarios:

**1. Editar archivo de CORS** (GatewayConfig.java):
```java
config.setAllowedOriginPatterns(Arrays.asList(
    "http://localhost:*",
    "http://tu-ip-servidor:*",      // ← Agregar IP de EC2
    "http://tu-dominio.com:*"       // ← Agregar dominio si tienes
));
```

**2. Actualizar application.yml del API Gateway**:
```yaml
server:
  port: ${SERVER_PORT:9090}
  
spring:
  application:
    name: api-gateway
  # Agregar esto si estás detrás de un proxy:
  cloud:
    gateway:
      x-forwarded-headers-enabled: true
```

**3. Actualizar .env del frontend**:
```env
VITE_API_URL=http://tu-ip-ec2:9090
# O si tienes dominio:
VITE_API_URL=https://api.tu-dominio.com
```

**4. Compilar y desplegar en EC2**:
```bash
# En el servidor EC2 (Linux)
git clone tu-repo
cd grupo-cordillera-servicios

# Compilar
mvn clean package -DskipTests

# Ejecutar en background
nohup java -jar api-gateway/target/api-gateway-1.0.0.jar &
nohup java -jar ms-ventas/target/ms-ventas-1.0.0.jar &
nohup java -jar ms-clientes/target/ms-clientes-1.0.0.jar &
# etc...
```

---

## 🔧 CAMBIOS POR COMPONENTE

### **API Gateway (api-gateway/src/main/resources/application.yml)**

```yaml
server:
  port: ${SERVER_PORT:9090}  # ← Cambiar si puerto está ocupado
```

Si cambias el puerto, actualiza también en el frontend.

---

### **Microservicios (ms-ventas/src/main/resources/application.properties)**

```properties
server.port=${SERVER_PORT:9091}  # ← Cambiar si está ocupado
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/db_ventas}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:postgres}
```

**Cambios necesarios si**:
- PostgreSQL está en otro servidor: `jdbc:postgresql://ip-servidor:5432/db_ventas`
- Usuario/password diferente: actualiza las variables

---

### **CORS (api-gateway/src/main/java/com/grupocordillera/gateway/config/GatewayConfig.java)**

**Actualmente permite**:
```
✅ http://localhost:* (cualquier puerto local)
✅ http://192.168.1.*:* (cualquier IP de red local)
✅ http://54.196.217.78:* (EC2 específica)
✅ http://*.local:* (nombres .local)
```

**Si necesitas un origen específico diferente**, edita:
```java
config.setAllowedOriginPatterns(Arrays.asList(
    "http://localhost:*",
    "http://192.168.x.x:*",        // Tu IP local
    "https://tu-dominio.com"        // Tu dominio en prod
));
```

---

## 📋 CHECKLIST SEGÚN ESCENARIO

### ✅ Escenario 1: Desarrollo local
- [ ] Ejecutar servicios en misma PC
- [ ] Acceder a `http://localhost:9090`
- No requiere cambios

### ✅ Escenario 2: Red local (mañana presentación)
- [ ] PC-B: Ejecutar `mvn spring-boot:run` en 3+ terminales
- [ ] PC-A: Frontend accede a `http://192.168.1.135:9090`
- [ ] CORS: Ya configurado ✅
- Cambios: Solo en .env del frontend

### ✅ Escenario 3: Producción EC2
- [ ] Compilar en EC2: `mvn clean package`
- [ ] Editar CORS con IP/dominio de EC2
- [ ] Actualizar .env del frontend
- [ ] Ejecutar con `nohup java -jar ...`
- [ ] Verificar puertos en Security Groups de AWS
- Cambios: GatewayConfig.java + application.yml + .env

---

## 🚨 PROBLEMAS COMUNES

| Problema | Solución |
|----------|----------|
| "Port X already in use" | Cambiar puerto en `application.yml`: `port: 9091` |
| "Cannot connect to database" | Verificar PostgreSQL corre y BD existen |
| "CORS error in browser" | Verificar IP en .env del frontend y en CORS |
| "Connection refused" | Verificar firewall permite puertos 9090-9095 |
| "502 Bad Gateway" | Microservicio no responde, verificar logs |

---

## 🎯 PARA LA PRESENTACIÓN DE MAÑANA

**Configuración actual (PC-B: 192.168.1.135)**:
- ✅ API Gateway: `http://192.168.1.135:9090`
- ✅ CORS: Aceptar `http://192.168.1.*:*`
- ✅ Frontend en PC-A: `.env` con `VITE_API_URL=http://192.168.1.135:9090`

**NO NECESITA CAMBIOS** - Está listo para correr en red local.

---

## 📞 PREGUNTAS FRECUENTES

**P: ¿Puedo correr todo en Docker?**
A: Sí, hay Dockerfiles en cada servicio. Usa `docker-compose.yml` (en raíz del proyecto).

**P: ¿Qué pasa si PostgreSQL está en otra máquina?**
A: Cambia `jdbc:postgresql://IP-BD:5432/db_ventas` en `application.properties`.

**P: ¿Cómo cambio el puerto del API Gateway?**
A: En `api-gateway/src/main/resources/application.yml`: `port: 9999` (por ejemplo).

**P: ¿Necesito cambiar nada en el frontend?**
A: Solo el `.env` con la IP/puerto del backend.

