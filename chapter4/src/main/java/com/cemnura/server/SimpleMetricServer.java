package com.cemnura.server;

import io.helidon.metrics.MetricsSupport; // <1>
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleMetricServer {

    private static final Logger logger = LogManager.getLogger(SimpleMetricServer.class);

    public static void main(String[] args) {

        WebServer server =
                WebServer.create( ServerConfiguration.builder().port(8080).build(),
                        Routing.builder()
                                .register(MetricsSupport.create()) // <2>
                                .build()
                );

        server.start().whenComplete((webServer, throwable) -> {
            logger.info("Server running on : http://localhost:{}", webServer.port());
        });
    }
}
