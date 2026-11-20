FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/target/estateease-opms-1.0.0.jar /app/app.jar
ENV DB_URL="jdbc:h2:mem:estateease;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
