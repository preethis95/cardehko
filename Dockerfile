# ─────────────────────────────────────────────────────────────────
# Stage 1 — BUILD  (Maven + JDK 8)
# Build context is the repo root, so all paths prefix with backend/
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:8-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and pom first (layer-cache friendly)
COPY backend/mvnw .
COPY backend/.mvn .mvn
COPY backend/pom.xml .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build the fat JAR (skip tests)
COPY backend/src src
RUN ./mvnw clean package -DskipTests -B

# ─────────────────────────────────────────────────────────────────
# Stage 2 — RUNTIME  (JRE 8 only)
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:8-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -S carfind && adduser -S carfind -G carfind
USER carfind

COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
