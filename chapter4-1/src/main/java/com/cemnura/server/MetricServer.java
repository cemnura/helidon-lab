package com.cemnura.server;

import com.cemnura.service.StatisticService;
import io.helidon.common.http.Http;
import io.helidon.metrics.prometheus.PrometheusSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MetricServer {

    private static final Logger logger = LogManager.getLogger(MetricServer.class);


    public static void main(String[] args) {

        WebServer server =
                WebServer.create( ServerConfiguration.builder()
                                .port(8080).build(),
                        Routing.builder()
                                .register(PrometheusSupport.create()) // <1>
                                .register("/statistic", StatisticService::new)  // <2>
                                .error(Exception.class, (req, res, ex) -> {
                                    res.send(Http.Status.BAD_REQUEST_400);
                                    res.send("Error Occurred");
                                })
                                .build()
                );

        server.start().whenComplete((webServer, throwable) -> {
            logger.info("Server running on : http://localhost:{}", webServer.port());
        });
    }
}
