# Use Eclipse Temurin JRE 21 as the base image
FROM eclipse-temurin:21-jre-jammy

# Set working directory in container
WORKDIR /app

# Add volumes for temporary files and external resources
VOLUME /tmp
VOLUME /app/resources

# Copy the built JAR file into the container
COPY build/libs/*.jar app.jar

# Environment variable for JVM configuration
ENV JAVA_OPTS=""

# Command to run the application with external resources path
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]