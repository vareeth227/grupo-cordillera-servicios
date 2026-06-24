# ms-ecommerce

Microservicio de gestión de pedidos online del sistema Grupo Cordillera. Administra el ciclo de vida completo de los pedidos desde su creación hasta la entrega, incluyendo los ítems asociados a cada orden.

## Tecnologías

- Java 21
- Spring Boot 3.3.0
- Spring Data JPA / Hibernate
- PostgreSQL 15
- Liquibase (control de versiones del esquema)
- Spring Cloud Netflix Eureka Client
- springdoc-openapi 2.3.0 (Swagger UI)
- JaCoCo 0.8.11
- Maven 3.9

## Puerto y base de datos

- Puerto: 9092
- Base de datos: db_ecommerce
- Puerto PostgreSQL: 5433

## Prerrequisitos

- Java 21 instalado
- Maven 3.9 instalado
- PostgreSQL 15 corriendo con la base de datos db_ecommerce creada
- Eureka Server corriendo en puerto 8761

## Ejecutar con Docker Compose (recomendado)

Desde la raíz del proyecto:

```bash
docker-compose up -d ms-ecommerce
```

Liquibase crea automáticamente las tablas pedidos e items_pedido e inserta datos semilla al arrancar.

## Ejecutar localmente con Maven

```bash
cd ms-ecommerce
mvn spring-boot:run
```

Variables de entorno requeridas:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/db_ecommerce
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
EUREKA_HOST=localhost
SERVER_PORT=9092
```

## Ejecutar pruebas unitarias

```bash
cd ms-ecommerce
mvn test
```

Para ver el reporte de cobertura JaCoCo después de correr las pruebas:

```bash
start target/site/jacoco/index.html
```

Resultado esperado: 6 pruebas unitarias en PedidoServiceImpl, 0 fallos, cobertura de capa service 100%.

## Documentación Swagger

Con el microservicio corriendo, acceder a:

```
http://localhost:9092/swagger-ui.html
```

## Endpoints disponibles

Todos los endpoints se exponen a través del API Gateway en `http://localhost:9090/api/ecommerce/`

```
GET    /ecommerce/pedidos                          Lista todos los pedidos
GET    /ecommerce/pedidos/{id}                     Obtiene un pedido por id
GET    /ecommerce/pedidos/cliente/{clienteId}      Pedidos de un cliente
GET    /ecommerce/pedidos/estado/{estado}          Pedidos por estado
POST   /ecommerce/pedidos                          Crea un nuevo pedido
PUT    /ecommerce/pedidos/{id}/estado?estado=X     Actualiza estado del pedido
DELETE /ecommerce/pedidos/{id}                     Elimina un pedido
```

Estados del pedido: PENDIENTE, CONFIRMADO, EN_ENVIO, ENTREGADO, CANCELADO

## Estructura del proyecto

```
ms-ecommerce/
├── src/
│   ├── main/java/com/grupocordillera/ecommerce/
│   │   ├── controller/    PedidoController
│   │   ├── service/       PedidoService, PedidoServiceImpl
│   │   ├── repository/    PedidoRepository, ItemPedidoRepository
│   │   ├── entity/        Pedido, ItemPedido
│   │   ├── dto/           PedidoDTO, ItemPedidoDTO
│   │   └── factory/       PedidoFactory
│   ├── main/resources/
│   │   ├── application.properties
│   │   └── db/changelog/  (migraciones Liquibase)
│   └── test/java/         PedidoServiceTest (6 pruebas Mockito)
├── Dockerfile
└── pom.xml
```
