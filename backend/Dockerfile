# ─────────────────────────────────────────────────────────────────
# Stage 1 — BUILD  (Maven + JDK 8)
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:8-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and pom first (layer-cache friendly)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies in a separate cached layer
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build the fat JAR (skip tests — no DB in CI)
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ─────────────────────────────────────────────────────────────────
# Stage 2 — RUNTIME  (JRE 8 only — much smaller image)
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:8-jre-alpine AS runtime

WORKDIR /app

# Create a non-root user for security
RUN addgroup -S carfind && adduser -S carfind -G carfind
USER carfind

# Copy only the fat JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render (and most PaaS) injects PORT at runtime — default 8080 for local docker run
ENV PORT=8080
EXPOSE 8080

# Pass PORT env var to Spring Boot so it binds on the correct port
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
