# Step 1: Build the app
FROM eclipse-temurin:21-jdk as build
WORKDIR /app

# Copy Maven wrapper & pom.xml first for dependency caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Ensure mvnw has execute permission
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the project and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Step 2: Run the app
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
