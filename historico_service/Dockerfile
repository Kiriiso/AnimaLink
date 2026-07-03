# ---- Etapa 1: BUILD (compila el .jar dentro del contenedor con JDK 21) ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiamos primero el wrapper y el pom para cachear la descarga de dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline || true

# Ahora el código fuente y empaquetamos
COPY src ./src
# Compilamos y renombramos el JAR a un nombre fijo para neutralizar fallos de comodines
RUN ./mvnw -B -q clean package -DskipTests && \
    mv target/*.jar target/app.jar

# ---- Etapa 2: RUNTIME (imagen liviana solo con el JRE 21) ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el ejecutable usando su nombre exacto y definitivo
COPY --from=build /app/target/app.jar app.jar

# Exponemos el puerto estándar de este microservicio (ajústalo si historico usa otro)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]