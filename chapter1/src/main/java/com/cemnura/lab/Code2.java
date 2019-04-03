package com.cemnura.lab;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

public class Code2 {
    public static void main(String[] args) {

        WebServer server = WebServer.create(
                Routing.builder()
                        .get("/greet/{name}",(
                                (req, res) -> {

                                    /*
                                        Fill in code.
                                    */

                                })
                        ).build()
        );

        server.start()
                .whenComplete((webServer, throwable) ->
                        System.out.println("http://localhost:"+ webServer.port()+ "/greet/"));


    }
}
