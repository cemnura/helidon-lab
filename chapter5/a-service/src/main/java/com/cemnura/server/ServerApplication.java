package com.cemnura.server;

import com.cemnura.service.BServiceClient;
import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.tracing.TracerBuilder;
import io.helidon.webserver.*;

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

    // tag::configuration[]
    private static ServerConfiguration serverConfiguration(Config config)
    {
        return ServerConfiguration.builder(config.get("server"))    // <1>
                .tracer(TracerBuilder.create(config.get("tracing.zipkin")).buildAndRegister())  // <2>
                .build();
    }
    // end::configuration[]
}
