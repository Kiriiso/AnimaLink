# ---- Etapa 1: BUILD (compila el .jar dentro del contenedor con JDK 21) ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
# Copiamos primero el wrapper y el pom para cachear la descarga de dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q -DskipTests dependency:go-offline || true
# Ahora el codigo fuente y empaquetamos
COPY src ./src
RUN ./mvnw -B -q clean package -DskipTests

# ---- Etapa 2: RUNTIME (imagen liviana solo con el JRE 21) ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
