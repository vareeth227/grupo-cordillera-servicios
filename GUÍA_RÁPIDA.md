# 🚀 GUÍA RÁPIDA - Ejecutar Backend sin Docker Desktop

## Requisitos (Verifica que tienes)

```powershell
java -version      # Debe ser Java 23 o superior
mvn -version       # Debe ser Maven 3.8 o superior
docker ps          # Docker debe estar corriendo (solo para BDs)
```

---

## 4 Pasos para tener todo funcionando

### 1️⃣ Inicia las Bases de Datos (30 segundos)

```powershell
docker-compose -f docker-compose-db-only.yml up -d
```

✅ Deberías ver 5 contenedores PostgreSQL creándose

Espera 30 segundos.

---

### 2️⃣ Compila todos los microservicios (5-10 minutos)

```powershell
.\build-all.ps1
```

✅ Debería terminar con: "Todos los servicios compilados exitosamente"

Esto genera 6 archivos JAR en cada carpeta `target/`

---

### 3️⃣ Ejecuta todos los servicios en paralelo (60 segundos)

```powershell
.\run-all.ps1
```

✅ Debería mostrar:
```
api-gateway ✓ http://localhost:9090
ms-clientes ✓ http://localhost:9095
ms-ventas ✓ http://localhost:9091
...
```

---

### 4️⃣ Verifica que funciona

```powershell
# En otra ventana de PowerShell, prueba:
curl http://localhost:9090/actuator/health
```

✅ Debería retornar: `{"status":"UP"}`

---

## ✅ ¡LISTO!

Tu backend está funcionando completamente:
- ✅ 5 Microservicios en Java
- ✅ 5 Bases de datos PostgreSQL  
- ✅ 1 API Gateway

### Ver Logs

```powershell
Get-Content logs/api-gateway.log -Tail 50
Get-Content logs/ms-clientes.log -Tail 50
```

### Detener Todo

Presiona `Ctrl+C` en la ventana de `run-all.ps1`

---

## 🆘 Si algo falla

| Problema | Solución |
|----------|----------|
| "Docker not found" | Instala Docker: https://www.docker.com/products/docker-desktop |
| "Maven not found" | Ya está en NetBeans: `C:\Program Files\NetBeans-23\netbeans\java\maven` |
| Servicio no inicia | Ver log: `Get-Content logs/ms-clientes.log` |
| Puertos en uso | `netstat -ano \| findstr "9090"` y mata el PID |

---

## 📊 Puertos Disponibles

| Servicio | Puerto |
|----------|--------|
| API Gateway | 9090 |
| ms-ventas | 9091 |
| ms-ecommerce | 9092 |
| ms-inventario | 9093 |
| ms-financiero | 9094 |
| ms-clientes | 9095 |

---

## 📝 Próximos pasos para conectar con el frontend

1. El frontend hará requests a: `http://localhost:9090`
2. CORS está configurado para: `http://localhost:5173`, `http://localhost:3000`
3. Todos los endpoints están listos en cada puerto individual

¡Listo para integrar! 🎉
