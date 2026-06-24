# ms-inventario

Microservicio de gestión de inventario del sistema Grupo Cordillera. Administra el catálogo de productos, controla el stock por almacén y genera alertas automáticas cuando el nivel de stock cae por debajo del umbral mínimo definido.

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

- Puerto: 9093
- Base de datos: db_inventario
- Puerto PostgreSQL: 5434

## Prerrequisitos

- Java 21 instalado
- Maven 3.9 instalado
- PostgreSQL 15 corriendo con la base de datos db_inventario creada
- Eureka Server corriendo en puerto 8761

## Ejecutar con Docker Compose (recomendado)

Desde la raíz del proyecto:

```bash
docker-compose up -d ms-inventario
```

Hibernate crea automáticamente las tablas productos y stock. El archivo data.sql inserta el catálogo semilla al arrancar.

## Ejecutar localmente con Maven

```bash
cd ms-inventario
mvn spring-boot:run
```

Variables de entorno requeridas:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5434/db_inventario
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
EUREKA_HOST=localhost
SERVER_PORT=9093
```

## Ejecutar pruebas unitarias

```bash
cd ms-inventario
mvn test
```

Para ver el reporte de cobertura JaCoCo después de correr las pruebas:

```bash
start target/site/jacoco/index.html
```

Resultado esperado: 16 pruebas unitarias en InventarioServiceImpl, 0 fallos, cobertura total 68%, cobertura de capa service 100%.

## Documentación Swagger

Con el microservicio corriendo, acceder a:

```
http://localhost:9093/swagger-ui.html
```

## Endpoints disponibles

Todos los endpoints se exponen a través del API Gateway en `http://localhost:9090/api/inventario/`

```
GET    /inventario/productos                        Lista todos los productos
GET    /inventario/productos/activos                Lista productos activos
POST   /inventario/productos                        Crea un producto
DELETE /inventario/productos/{id}                   Elimina un producto
GET    /inventario/stock                            Lista todo el stock
GET    /inventario/stock/producto/{productoId}      Stock de un producto
POST   /inventario/stock                            Registra stock
PUT    /inventario/stock/{id}?cantidad=N            Actualiza cantidad de stock
DELETE /inventario/stock/{id}                       Elimina registro de stock
GET    /inventario/alertas                          Productos bajo umbral mínimo
```

## Estructura del proyecto

```
ms-inventario/
├── src/
│   ├── main/java/com/grupocordillera/inventario/
│   │   ├── controller/    InventarioController
│   │   ├── service/       InventarioService, InventarioServiceImpl
│   │   ├── repository/    ProductoRepository, StockRepository
│   │   ├── entity/        Producto, Stock
│   │   ├── dto/           ProductoDTO, StockDTO, AlertaInventarioDTO
│   │   └── factory/       ProductoFactory
│   ├── main/resources/
│   │   ├── application.properties
│   │   └── data.sql       (datos semilla)
│   └── test/java/         InventarioServiceTest (16 pruebas Mockito)
├── Dockerfile
└── pom.xml
```
