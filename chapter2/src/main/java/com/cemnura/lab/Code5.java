package com.cemnura.lab;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.PollingStrategies;
import io.helidon.config.git.GitConfigSourceBuilder;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.net.URI;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.helidon.config.ConfigSources.classpath;

public class Code5 {

    private static final Logger logger = LogManager.getLogger(Code5.class);
    private static String greetings = "Hello";

    public static void main(String[] args) {

        Config config = Config.create(
                ConfigSources.file("chapter2\\src\\main\\resources\\application.yaml")  // <1>
                        .pollingStrategy(PollingStrategies.regular(Duration.ofSeconds(2))) // <2>
                ).get("webserver");

        config.onChange((changedNode) -> { // <3>
            Code5.configLogger(changedNode.get("loglevel").as(String.class).get());
            Code5.configSettings(changedNode);
        });

        WebServer webServer =
                WebServer.create(ServerConfiguration.create(config),
                        Routing.builder()
                                .any((req, res) -> {

                                    logger.error("ERROR");
                                    logger.warn("WARN");
                                    logger.info("INFO");

                                    res.send(greetings);
                                })
                        .build()
                );

        webServer.start()
                .whenComplete((server, throwable) -> {
                   logger.info("Server started on http://localhost:{}", server.port());
                });


    }


    public static void configLogger(String level)
    {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration conf = ctx.getConfiguration();
        LoggerConfig loggerConfig = conf.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(Level.valueOf(level));
        ctx.updateLoggers();
    }

    public static void configSettings(Config config)
    {
        greetings = config.get("greetings").as(String.class).get();
    }
}
