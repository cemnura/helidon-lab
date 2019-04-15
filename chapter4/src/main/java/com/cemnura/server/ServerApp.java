package com.cemnura.server;

import io.helidon.common.http.Http;
import io.helidon.webserver.*;
import io.helidon.config.ConfigSources;
import io.helidon.config.Config;

import com.cemnura.service.StatisticService;

public class ServerApp {

    public static void main(String[] args) {

        WebServer.create(ServerApp::createConfig,ServerApp::createRouting)
                .start()
                .whenComplete((webServer, throwable) -> {
                    System.out.println("http://localhost:" + webServer.port());
                });

    }

    private static Routing createRouting()
    {

        return Routing.builder()
                .register("/statistic",StatisticService::new)
                .error(Exception.class, (req, res, ex) -> {
                    res.status(Http.Status.BAD_REQUEST_400);
                    res.send("Unsupported Operation");
                })
                .build();
    }

    private static ServerConfiguration createConfig()
    {
        return ServerConfiguration
                .builder(Config.builder()
                        .sources(ConfigSources.file("chapter4/src/main/resources/application.yaml")).build()
                .get("webserver"))
                .build();
    }

}
