# Build stage
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Convert line endings for Windows users and make executable
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Forzar el uso de Java 25
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Download dependencies
RUN ./mvnw dependency:go-offline -B

COPY src ./src

# Build con flags específicos para Java 25
RUN ./mvnw clean package -DskipTests -Dmaven.compiler.release=25

# Run stage
FROM eclipse-temurin:25-jre

WORKDIR /app

# Install curl for healthcheck/debugging
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# JVM flags and dynamic PORT
ENTRYPOINT ["sh", "-c", "java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=${PORT:-8080} -jar app.jar"]
