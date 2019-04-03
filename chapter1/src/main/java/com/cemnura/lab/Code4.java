package com.cemnura.lab;

import io.helidon.common.http.Http;
import io.helidon.media.jsonp.common.JsonProcessing;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Code4 {

    private static final Logger logger = LogManager.getLogger(Code4.class);


    public static void main(String[] args) {

        WebServer server =
                WebServer.create(
                        Routing.builder()
                                .register(JsonSupport.create(JsonProcessing.create()))
                                .any((req, res) -> {
                                    logger.info("Got Request"); // <1>
                                    res.status(Http.Status.ACCEPTED_202); // <2>
                                    res.send(); // <3>
                                })
                                .build()
                );

        server.start()
                .whenComplete((webServer, throwable) ->
                        System.out.println("http://localhost:"+ webServer.port()+ "/greet"));


    }
}
