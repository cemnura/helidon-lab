package com.cemnura.lab;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import java.util.Collections;

public class Code9 {

    private static final Logger logger = LogManager.getLogger(Code6_Solution.class);
    private static final JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Collections.emptyMap());

    public static void main(String[] args) {

        WebServer server =
                WebServer.create(
                        Routing.builder()
                                /*
                                    Fill in Block
                                 */
                                .build()
                );

        server.start()
                .whenComplete((webServer, throwable) -> {
                    System.out.println("http://localhost:" + webServer.port());
                });


    }
}
