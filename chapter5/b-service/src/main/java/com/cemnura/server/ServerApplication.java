package com.cemnura.server;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.tracing.TracerBuilder;
import io.helidon.webserver.*;
import io.opentracing.Tracer;

import java.net.InetAddress;
import java.net.URI;

public class ServerApplication {
    public static void main(String[] args) {
        Config config = Config.create(ConfigSources.classpath("application.yaml"));
        ServerConfiguration conf = serverConfiguration(config);

        WebServer ws = WebServer.create(conf, Routing.builder()
                .any((req, res) -> res.send("Hello from B service"))
                .build());

        ws.start().whenComplete((webServer, throwable) -> System.out.println("Running on http://localhost:" +  webServer.port()));

    }

    private static ServerConfiguration serverConfiguration(Config config)
    {
        return ServerConfiguration.builder(config.get("server"))
                .tracer(TracerBuilder.create(config.get("tracing.zipkin")).buildAndRegister())
                .build();
    }

}
