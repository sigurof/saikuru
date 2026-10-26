# syntax=docker/dockerfile:1

# --- Build: compile the Spring Boot jar with the Gradle wrapper ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Download dependencies first for caching
COPY gradlew settings.gradle.kts build.gradle.kts gradle.lockfile lombok.config ./
COPY gradle gradle
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon --quiet > /dev/null

COPY src src
# Build the fat Jar
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar --no-daemon --quiet

# Unpacks Boot's fat jar into plain directory and sorts them by how often they change.
# Allows more efficient caching of the COPY commands in the Runtime image below
RUN java -Djarmode=tools -jar build/libs/saikuru.jar extract --layers --destination extracted

# --- Runtime: JRE only, running as a non-root user ---
FROM eclipse-temurin:21-jre
RUN useradd --system --uid 1001 saikuru
WORKDIR /application

COPY --from=build /workspace/extracted/dependencies/ ./
COPY --from=build /workspace/extracted/spring-boot-loader/ ./
COPY --from=build /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build /workspace/extracted/application/ ./

USER saikuru
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "saikuru.jar"]
