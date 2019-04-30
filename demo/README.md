# Helidon Demo Application

This demo project consist of the following services

* hero-service
* quote-service

Hero service is a client of quote service the quotes of heroes will be retrieved from the quote service.
Tracing is consistent between services therefore we will be able to observer the trace span on each request.

## Application

This demo project combines Helidon features such as;

* Prometheus
* Zipkin
* Docker

## Building

You must be in the root directory for the following commands to work.

**Code**:

    HelidonLab>./gradlew demo:hero:jar
    HelidonLab>./gradlew demo:quote:jar
    cd demo
    docker-compose up

Now 5 docker container should be running.

* hero-service
* quote-service
* Prometheus
* Zipkin
* mysql database

hero-service can be accessed at:

    http://localhost:8081
quote-service can be accessed at:

    http://localhost:8082


**Now try the following**:

    curl -X GET http://localhost:8081/api/hero/heroes
    curl -X GET http://localhost:8081/api/hero/villains
    curl -X GET http://localhost:8081/api/hero/random
    curl -X GET http://localhost:8081/api/hero/id/3 
    curl -X GET http://localhost:8081/api/hero/heroes?startsWith=B
    curl -X GET http://localhost:8081/api/hero/villains?startsWith=M
    curl -X GET http://localhost:8081/api/hero/id/3?getQuotes=true 
    curl -X GET http://localhost:8082/quote/Magneto

**Observe the request from**:

**Prometheus**:

    http://localhost:9090/graph
    
**Zipkin**:

    http://localhost:9411