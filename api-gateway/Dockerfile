# ============================================================
# Etapa 1: Compilación (Builder)
# Usa Maven para compilar y empaquetar el JAR
# ============================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar el POM primero para aprovechar el caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# Etapa 2: Runtime
# Solo incluye el JRE y el JAR compilado (imagen más pequeña)
# ============================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar solo el JAR compilado desde la etapa builder
COPY --from=builder /app/target/*.jar app.jar

# Puerto del API Gateway
EXPOSE 9090

# Usuario no-root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Ejecutar el JAR con configuración de JVM optimizada para contenedores
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
