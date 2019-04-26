package com.cemnura.lab;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Code1 {

    private static final Logger logger = LogManager.getLogger(Code1.class);

    public static void main(String[] args) {

        WebServer webServer =
                WebServer.create(
                        ServerConfiguration.builder() // <1>
                                .port(8080) // <2>
                        .build(),   // <3>
                        Routing.builder()
                                .any((req, res) -> {
                                    res.send("Im on " + req.webServer().port());
                                })
                        .build()
                );

        webServer.start()
                .whenComplete((server, throwable) -> {
                   logger.info("Server started on http://localhost:{}", server.port());
                });

    }

}
