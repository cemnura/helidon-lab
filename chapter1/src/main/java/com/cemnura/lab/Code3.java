package com.cemnura.lab;

import io.helidon.media.jsonp.common.JsonProcessing;
import io.helidon.media.jsonp.server.JsonSupport;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.util.Collections;

public class Code3 {

    private static final JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Collections.emptyMap());

    public static void main(String[] args) {

        WebServer server =
                WebServer.create(
                        Routing.builder()
                                .register(JsonSupport.create(JsonProcessing.create()))  // <1>
                                .post("/greet/{name}",(req, res) -> {
                                    JsonObject json = jsonFactory.createObjectBuilder().add("message", "Greetings " + req.path().param("name")).build();    // <2>
                                    res.send(json); // <3>
                                }).build()
                        );

        server.start()
                .whenComplete((webServer, throwable) ->
                        System.out.println("http://localhost:"+ webServer.port()+ "/greet"));


    }
}
