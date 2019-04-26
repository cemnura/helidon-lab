package com.cemnura.server;

import com.cemnura.exception.CharacterNotFoundException;
import com.cemnura.service.QuoteHandler;
import io.helidon.common.http.Http;
import io.helidon.config.Config;
import io.helidon.metrics.prometheus.PrometheusSupport;
import io.helidon.tracing.TracerBuilder;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

public class QuoteServer {

    private static final Logger logger = LogManager.getLogger(QuoteServer.class);

    public static void main(String[] args) {

        WebServer webServer = WebServer.create(createConfiguration(Config.create()), createRouting());

        webServer
                .start()
                .whenComplete(QuoteServer::printServerInfo);
    }

    private static Routing createRouting()
    {
        return Routing.builder()
                .register(PrometheusSupport.create())
                .register("/quote", QuoteHandler::new)
                .error(CharacterNotFoundException.class, (req, res, ex) -> {
                    res.status(Http.Status.NOT_FOUND_404);
                    res.send(ex.getMessage());
                })
                .error(ConstraintViolationException.class, (req, res, ex) -> {
                    res.status(Http.Status.BAD_REQUEST_400);
                    res.send("Character already exist");
                })
                .build();
    }

    private static ServerConfiguration createConfiguration(Config config)
    {

        return ServerConfiguration.builder(config.get("server"))
                .tracer(TracerBuilder.create(config.get("tracing.zipkin")).buildAndRegister())
                .build();
    }


    public static void printServerInfo(WebServer ws, Throwable throwable)
    {
        if (null != throwable) {
            logger.error("Server failed to start");
        }else {
            logger.info("Server running on: http://localhost:" + ws.port());
        }
    }

}
