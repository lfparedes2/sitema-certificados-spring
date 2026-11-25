# Etapa 1: Construcción del proyecto con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
# Agregamos -Dexec.skip=true para saltar compile-tailwind de Windows
RUN mvn clean package -DskipTests -Dexec.skip=true

# Etapa 2: Imagen ligera para ejecución en producción
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
