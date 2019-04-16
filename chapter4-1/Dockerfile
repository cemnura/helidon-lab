# Basic JVM Environment
FROM openjdk:8
# Designate default working directory in container
WORKDIR /usr
# Copy dependencies into container
COPY /build/libs/libs ./app/libs
# Copy JAR File
COPY /build/libs/chapter4-1.jar ./app
# Run the command when docker container starts
CMD ["java", "-jar", "app/chapter4-1.jar"]