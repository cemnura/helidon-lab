package com.cemnura.lab;

import io.helidon.webserver.Routing; // <1>
import io.helidon.webserver.WebServer; // <1>

public class Code1 {
    public static void main(String[] args) {

        WebServer server = WebServer.create(
                        Routing.builder() // <2>
                                .get("/greet",(
                                        (req, res) -> {
                                            res.send("Greetings!!!");   // <3>
                                        })
                                ).build() // <4>
                        );

        server.start()  // <5>
                .whenComplete((webServer, throwable) ->
                        System.out.println("http://localhost:"+ webServer.port()+ "/greet"));

    }
}
