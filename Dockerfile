# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Convert line endings for Windows users and make executable
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Ensure JDK 21 from the base image is used

# Download dependencies
RUN ./mvnw dependency:go-offline -B

COPY src ./src

# Build
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Install curl for healthcheck/debugging
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Optimized JVM flags for Railway (512MB plan)
ENTRYPOINT ["sh", "-c", "java --enable-preview \
    -Xms128m \
    -Xmx384m \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=100 \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} \
    -Dserver.port=${PORT:-8080} \
    -jar app.jar"]
