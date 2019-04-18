package com.cemnura.server;

import com.cemnura.service.BServiceClient;
import io.helidon.common.http.Http;
import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.spi.Source;
import io.helidon.tracing.TracerBuilder;
import io.helidon.tracing.zipkin.ZipkinTracer;
import io.helidon.tracing.zipkin.ZipkinTracerBuilder;
import io.helidon.webserver.*;
import io.opentracing.Tracer;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

import static io.helidon.config.ConfigSources.classpath;
import static io.helidon.config.ConfigSources.file;

public class ServerApplication {

    public static void main(String[] args) {
        Config config = Config.create(ConfigSources.classpath("application.yaml"));
        ServerConfiguration conf = serverConfiguration(config);

        WebServer ws = WebServer.create(conf, Routing.builder()
                .any("/", (req, res) -> res.send("Hello from A service"))
                .get("/b", (req, res) -> {
                    BServiceClient client =  new BServiceClient(config);

                    String b_services_response = client.callService(req.spanContext());

                    res.send("Service A got the message from service B: " + b_services_response);
                })
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
