# ---------- Etapa 1: compilar con Maven ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---------- Etapa 2: imagen liviana solo con el JRE ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/api-techservices.jar app.jar
# Limita la memoria para el plan gratuito de Render (512 MB)
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Duser.timezone=America/Lima"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
