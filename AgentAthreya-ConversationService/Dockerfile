# Start with OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set work directory
WORKDIR /app

# Copy the jar file from target folder
COPY target/AgentAthreya-ConversationService-0.0.1-SNAPSHOT.jar app.jar

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose port 8080 (because Spring Boot runs on 8080 by default)
EXPOSE 8080
