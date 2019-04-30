# Basic JVM Environment
FROM openjdk:8
# Designate default working directory in container
WORKDIR /usr
# Copy dependencies into container
COPY /build/libs/libs ./app/libs
# Copy resources into container
COPY /build/resources/main .
# Copy JAR File
COPY /build/libs/quote.jar ./app
# Run the command when docker container starts
CMD ["java", "-jar", "app/quote.jar"]