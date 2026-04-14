# Etapa 1: Compilación
FROM maven:3.8.5-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
# Etapa 2: Ejecución
FROM eclipse-temurin:17-jdk-alpine
# Agregamos un punto para asegurar la ruta relativa correcta
COPY --from=build /target/*.jar app.jar 
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]