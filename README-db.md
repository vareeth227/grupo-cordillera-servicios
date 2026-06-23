# Grupo Cordillera — Bases de Datos

Contiene las 5 instancias PostgreSQL del sistema (una por microservicio).

> Iniciar **este repositorio primero**, antes de los microservicios.

---

## Requisito

**Docker Desktop** instalado y en ejecución.  
Descarga: https://www.docker.com/products/docker-desktop

Verificar que Docker está activo:
```powershell
docker --version
```

---

## Pasos para iniciar

Abre **PowerShell** en la carpeta de este repositorio.

**Paso 1 — Crear la red compartida de Docker**
```powershell
docker network create cordillera-net
```
> Si aparece el error "network with name cordillera-net already exists", continúa al paso 2.

**Paso 2 — Levantar las bases de datos**
```powershell
docker-compose -f docker-compose-db.yml up -d
```

**Paso 3 — Verificar que los 5 contenedores están activos**
```powershell
docker ps
```

Debes ver estos contenedores con estado `Up`:

| Contenedor    | Base de datos   | Puerto |
|---------------|-----------------|--------|
| db-ventas     | db_ventas       | 5432   |
| db-ecommerce  | db_ecommerce    | 5433   |
| db-inventario | db_inventario   | 5434   |
| db-financiero | db_financiero   | 5435   |
| db-clientes   | db_clientes     | 5436   |

---

## Detener

```powershell
docker-compose -f docker-compose-db.yml down
```

> Los datos se conservan aunque detengas los contenedores.  
> Para borrar todo y comenzar desde cero: `docker-compose -f docker-compose-db.yml down -v`
