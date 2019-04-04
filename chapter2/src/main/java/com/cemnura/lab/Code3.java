package com.cemnura.lab;

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.helidon.config.ConfigSources.classpath;

public class Code3 {

    private static final Logger logger = LogManager.getLogger(Code3.class);

    public static void main(String[] args) {

        WebServer webServer =
                WebServer.create(
                        ServerConfiguration.builder(
                                Config.create(classpath("application.json")) // <1>
                                        .get("webserver")) // <2>
                                .build(),
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
