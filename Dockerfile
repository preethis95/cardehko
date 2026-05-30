# ─────────────────────────────────────────────────────────────────
# Stage 1 — BUILD
# Uses the official Maven image with OpenJDK 8 — no mvnw wrapper needed
# ─────────────────────────────────────────────────────────────────
FROM maven:3.8.8-openjdk-8-slim AS build

WORKDIR /app

# Cache dependencies first (only re-runs when pom.xml changes)
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# Build the fat JAR
COPY backend/src ./src
RUN mvn clean package -DskipTests -B --no-transfer-progress

# ─────────────────────────────────────────────────────────────────
# Stage 2 — RUNTIME
# Debian-based JRE 8 (more compatible than Alpine for Spring Boot)
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:8-jre-jammy AS runtime

WORKDIR /app

# Non-root user (Debian addgroup/adduser syntax)
RUN groupadd --system carfind && useradd --system --gid carfind carfind
USER carfind

COPY --from=build /app/target/*.jar app.jar

# Render injects PORT at runtime; default 8080 for local docker run
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Xmx256m -Xms128m -jar app.jar --server.port=${PORT}"]
