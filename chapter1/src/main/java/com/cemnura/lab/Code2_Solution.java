package com.cemnura.lab;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

public class Code2_Solution {
    public static void main(String[] args) {

        WebServer server = WebServer.create(
                Routing.builder()
                        .get("/greet/{name}",(  // <1>
                                (req, res) -> {
                                    String name = req.path().param("name"); // <2>
                                    res.send("Greetings, " + name + "!!!"); // <3>
                                })
                        ).build()
        );

        server.start()
                .whenComplete((webServer, throwable) ->
                        System.out.println("http://localhost:"+ webServer.port()+ "/greet/hello"));


    }
}
