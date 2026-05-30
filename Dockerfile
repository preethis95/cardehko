# ─────────────────────────────────────────────────────────────────
# Stage 1 — BUILD
# eclipse-temurin:8-jdk-focal is a confirmed valid Docker Hub tag
# Uses the Maven Wrapper (mvnw) — downloads Maven automatically
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:8-jdk-focal AS build

WORKDIR /app

# Copy Maven wrapper first so dependency layer is cached
COPY backend/mvnw .
COPY backend/.mvn .mvn
COPY backend/pom.xml .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build fat JAR
COPY backend/src src
RUN ./mvnw clean package -DskipTests -B

# ─────────────────────────────────────────────────────────────────
# Stage 2 — RUNTIME
# eclipse-temurin:8-jre-focal — Ubuntu Focal, full glibc, no Alpine issues
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:8-jre-focal AS runtime

WORKDIR /app

RUN groupadd --system carfind && useradd --system --gid carfind carfind
USER carfind

COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Xmx256m -Xms128m -jar app.jar --server.port=${PORT}"]
