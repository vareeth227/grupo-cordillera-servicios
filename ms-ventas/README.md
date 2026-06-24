# ms-ventas

Microservicio de gestión de ventas del sistema Grupo Cordillera. Administra los puntos de venta físicos, registra transacciones (ventas y devoluciones) y genera reportes diarios de actividad comercial.

## Tecnologías

- Java 21
- Spring Boot 3.3.0
- Spring Data JPA / Hibernate
- PostgreSQL 15
- Spring Cloud Netflix Eureka Client
- springdoc-openapi 2.3.0 (Swagger UI)
- JaCoCo 0.8.11
- Maven 3.9

## Puerto y base de datos

- Puerto: 9091
- Base de datos: db_ventas
- Puerto PostgreSQL: 5432

## Prerrequisitos

- Java 21 instalado
- Maven 3.9 instalado
- PostgreSQL 15 corriendo con la base de datos db_ventas creada
- Eureka Server corriendo en puerto 8761

## Ejecutar con Docker Compose (recomendado)

Desde la raíz del proyecto ejecutar el stack completo:

```bash
docker-compose up -d ms-ventas
```

Esto levanta automáticamente la base de datos db_ventas y el microservicio con todas las dependencias configuradas.

## Ejecutar localmente con Maven

```bash
cd ms-ventas
mvn spring-boot:run
```

Variables de entorno requeridas:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/db_ventas
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
EUREKA_HOST=localhost
SERVER_PORT=9091
```

## Ejecutar pruebas unitarias

```bash
cd ms-ventas
mvn test
```

Para ver el reporte de cobertura JaCoCo después de correr las pruebas:

```bash
start target/site/jacoco/index.html
```

Resultado esperado: 12 pruebas unitarias en VentaServiceImpl, 0 fallos, cobertura de capa service 100%.

## Documentación Swagger

Con el microservicio corriendo, acceder a:

```
http://localhost:9091/swagger-ui.html
```

## Endpoints disponibles

Todos los endpoints se exponen a través del API Gateway en `http://localhost:9090/api/ventas/`

```
GET    /ventas/puntos                          Lista todos los puntos de venta
GET    /ventas/puntos/activos                  Lista puntos de venta activos
POST   /ventas/puntos                          Crea un nuevo punto de venta
DELETE /ventas/puntos/{id}                     Elimina un punto de venta
GET    /ventas/transacciones                   Lista todas las transacciones
POST   /ventas/transacciones/venta             Registra una venta
POST   /ventas/transacciones/devolucion        Registra una devolución
DELETE /ventas/transacciones/{id}              Elimina una transacción
GET    /ventas/reporte-diario?fecha=YYYY-MM-DD Reporte del día indicado
```

## Estructura del proyecto

```
ms-ventas/
├── src/
│   ├── main/java/com/grupocordillera/ventas/
│   │   ├── controller/    VentaController
│   │   ├── service/       VentaService, VentaServiceImpl
│   │   ├── repository/    PuntoDeVentaRepository, TransaccionRepository
│   │   ├── entity/        PuntoDeVenta, Transaccion
│   │   ├── dto/           PuntoDeVentaDTO, TransaccionDTO, ReporteDiarioDTO
│   │   └── factory/       TransaccionFactory
│   ├── main/resources/
│   │   ├── application.properties
│   │   └── data.sql       (datos semilla)
│   └── test/java/         VentaServiceTest (12 pruebas Mockito)
├── Dockerfile
└── pom.xml
```
