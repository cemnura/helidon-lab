package com.cemnura.lab;

import io.helidon.common.http.HttpRequest;
import io.helidon.config.Config;
import io.helidon.config.git.GitConfigSourceBuilder;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static io.helidon.config.ConfigSources.classpath;

public class Code4 {

    private static final Logger logger = LogManager.getLogger(Code4.class);

    public static void main(String[] args) {

        Config config =
                Config.create(
                    GitConfigSourceBuilder
                            .create("/chapter2/src/main/resources/application.yaml")    // <1>
                            .uri(URI.create("https://github.com/cemnura/helidon-lab.git"))  // <2>
                            .branch("chapter2"))    // <3>
                .get("webserver");  // <4>



        WebServer webServer =
                WebServer.create(ServerConfiguration.create(config),
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
