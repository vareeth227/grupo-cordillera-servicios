# ms-financiero

Microservicio de gestión financiera del sistema Grupo Cordillera. Registra ingresos y egresos operacionales, permite filtrar movimientos por período y calcula KPIs financieros como utilidad bruta y margen de utilidad.

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

- Puerto: 9094
- Base de datos: db_financiero
- Puerto PostgreSQL: 5435

## Prerrequisitos

- Java 21 instalado
- Maven 3.9 instalado
- PostgreSQL 15 corriendo con la base de datos db_financiero creada
- Eureka Server corriendo en puerto 8761

## Ejecutar con Docker Compose (recomendado)

Desde la raíz del proyecto:

```bash
docker-compose up -d ms-financiero
```

Hibernate crea automáticamente las tablas ingresos y egresos. El archivo data.sql inserta movimientos semilla al arrancar.

## Ejecutar localmente con Maven

```bash
cd ms-financiero
mvn spring-boot:run
```

Variables de entorno requeridas:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5435/db_financiero
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
EUREKA_HOST=localhost
SERVER_PORT=9094
```

## Ejecutar pruebas unitarias

```bash
cd ms-financiero
mvn test
```

Para ver el reporte de cobertura JaCoCo después de correr las pruebas:

```bash
start target/site/jacoco/index.html
```

Resultado esperado: 12 pruebas unitarias en FinancieroServiceImpl, 0 fallos, cobertura de capa service 100%.

## Documentación Swagger

Con el microservicio corriendo, acceder a:

```
http://localhost:9094/swagger-ui.html
```

## Endpoints disponibles

Todos los endpoints se exponen a través del API Gateway en `http://localhost:9090/api/financiero/`

```
GET    /financiero/ingresos                                    Lista todos los ingresos
GET    /financiero/ingresos/periodo?inicio=YYYY-MM-DD&fin=...  Ingresos por período
POST   /financiero/ingresos                                    Registra un ingreso
DELETE /financiero/ingresos/{id}                               Elimina un ingreso
GET    /financiero/egresos                                     Lista todos los egresos
GET    /financiero/egresos/periodo?inicio=YYYY-MM-DD&fin=...   Egresos por período
POST   /financiero/egresos                                     Registra un egreso
DELETE /financiero/egresos/{id}                                Elimina un egreso
GET    /financiero/kpis?inicio=YYYY-MM-DD&fin=YYYY-MM-DD       KPIs del período
```

Categorías de ingreso: VENTAS, SERVICIOS, INVERSIONES, OTROS
Categorías de egreso: SUELDOS, ARRIENDO, INSUMOS, SERVICIOS, OTROS

## Estructura del proyecto

```
ms-financiero/
├── src/
│   ├── main/java/com/grupocordillera/financiero/
│   │   ├── controller/    FinancieroController
│   │   ├── service/       FinancieroService, FinancieroServiceImpl
│   │   ├── repository/    IngresoRepository, EgresoRepository
│   │   ├── entity/        Ingreso, Egreso
│   │   ├── dto/           IngresoDTO, EgresoDTO, KpiFinancieroDTO
│   │   └── factory/       MovimientoFactory
│   ├── main/resources/
│   │   ├── application.properties
│   │   └── data.sql       (datos semilla)
│   └── test/java/         FinancieroServiceTest (12 pruebas Mockito)
├── Dockerfile
└── pom.xml
```
