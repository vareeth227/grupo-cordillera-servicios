# ms-clientes

Microservicio de CRM y autenticación del sistema Grupo Cordillera. Gestiona el registro de clientes, tickets de atención al cliente y es el único microservicio que genera tokens JWT para autenticar el acceso al sistema a través del API Gateway.

## Tecnologías

- Java 21
- Spring Boot 3.3.0
- Spring Data JPA / Hibernate
- PostgreSQL 15
- Liquibase (control de versiones del esquema)
- Spring Cloud Netflix Eureka Client
- JWT (jjwt 0.12.3)
- BCrypt (spring-security-crypto)
- springdoc-openapi 2.3.0 (Swagger UI)
- JaCoCo 0.8.11
- Maven 3.9

## Puerto y base de datos

- Puerto: 9095
- Base de datos: db_clientes
- Puerto PostgreSQL: 5436

## Prerrequisitos

- Java 21 instalado
- Maven 3.9 instalado
- PostgreSQL 15 corriendo con la base de datos db_clientes creada
- Eureka Server corriendo en puerto 8761

## Ejecutar con Docker Compose (recomendado)

Desde la raíz del proyecto:

```bash
docker-compose up -d ms-clientes
```

Liquibase crea automáticamente las tablas clientes, tickets_atencion y usuarios, e inserta datos semilla al arrancar.

## Ejecutar localmente con Maven

```bash
cd ms-clientes
mvn spring-boot:run
```

Variables de entorno requeridas:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5436/db_clientes
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
EUREKA_HOST=localhost
SERVER_PORT=9095
JWT_SECRET=clave-secreta-minimo-32-caracteres
```

## Ejecutar pruebas unitarias

```bash
cd ms-clientes
mvn test
```

Para ver el reporte de cobertura JaCoCo después de correr las pruebas:

```bash
start target/site/jacoco/index.html
```

Resultado esperado: 14 pruebas unitarias en ClienteServiceImpl, 0 fallos, cobertura de capa service 100%.

## Documentación Swagger

Con el microservicio corriendo, acceder a:

```
http://localhost:9095/swagger-ui.html
```

## Endpoints disponibles

Todos los endpoints se exponen a través del API Gateway en `http://localhost:9090/api/clientes/`

```
POST   /clientes/auth/login                         Genera token JWT
GET    /clientes                                    Lista todos los clientes
GET    /clientes/activos                            Lista clientes activos
GET    /clientes/{id}                               Obtiene cliente por id
POST   /clientes                                    Registra nuevo cliente
PATCH  /clientes/{id}/desactivar                    Desactiva un cliente
DELETE /clientes/{id}                               Elimina un cliente
GET    /clientes/tickets                            Lista todos los tickets
GET    /clientes/{id}/tickets                       Tickets de un cliente
GET    /clientes/tickets/estado/{estado}            Tickets por estado
POST   /clientes/tickets                            Crea ticket de atención
PUT    /clientes/tickets/{id}/estado?estado=X       Actualiza estado del ticket
DELETE /clientes/tickets/{id}                       Elimina un ticket
```

Estados de ticket: ABIERTO, EN_PROCESO, RESUELTO, CERRADO
Categorías de ticket: RECLAMO, CONSULTA, SOPORTE_TECNICO, DEVOLUCION

## Flujo de autenticación

```
POST /api/clientes/auth/login
Body: { "email": "admin@grupocordillera.cl", "password": "admin123" }

Respuesta: { "token": "eyJhbGci...", "tipo": "Bearer", "email": "...", "rol": "ADMIN" }

Usar el token en todos los demás endpoints:
Header: Authorization: Bearer eyJhbGci...
```

## Estructura del proyecto

```
ms-clientes/
├── src/
│   ├── main/java/com/grupocordillera/clientes/
│   │   ├── controller/    ClienteController, AuthController
│   │   ├── service/       ClienteService, ClienteServiceImpl, AuthService
│   │   ├── repository/    ClienteRepository, TicketAtencionRepository, UsuarioRepository
│   │   ├── entity/        Cliente, TicketAtencion, Usuario
│   │   ├── dto/           ClienteDTO, TicketAtencionDTO, LoginRequest, LoginResponse
│   │   └── factory/       ClienteFactory
│   ├── main/resources/
│   │   ├── application.properties
│   │   └── db/changelog/  (migraciones Liquibase)
│   └── test/java/         ClienteServiceTest (14 pruebas Mockito)
├── Dockerfile
└── pom.xml
```
