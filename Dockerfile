# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Set Maven options for Railway's memory limits
ENV MAVEN_OPTS="-Xmx512m -Xms256m"

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Convert line endings for Windows users and make executable
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Download dependencies with reduced memory
RUN ./mvnw dependency:go-offline -B || true

COPY src ./src

# Build with reduced memory and parallel execution disabled
RUN ./mvnw clean package -DskipTests -T 1

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Install curl for healthcheck/debugging
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Optimized JVM flags for Railway (512MB plan)
ENTRYPOINT ["sh", "-c", "java \
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
