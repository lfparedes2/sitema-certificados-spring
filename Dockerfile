# Etapa 1: Construcción del proyecto con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente y generamos el archivo .jar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera para ejecución en producción
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el .jar generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto por defecto
EXPOSE 8080

# Limitamos el uso de memoria RAM al 75% para no exceder los 512MB gratis de Render
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]