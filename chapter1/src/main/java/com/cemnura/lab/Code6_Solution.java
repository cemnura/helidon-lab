package com.cemnura.lab;

import io.helidon.common.http.Http;
import io.helidon.media.jsonp.common.JsonProcessing;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class Code6_Solution {

    private static final Logger logger = LogManager.getLogger(Code6_Solution.class);


    public static void main(String[] args) {

        AtomicInteger count = new AtomicInteger();

        WebServer server =
                WebServer.create(
                        Routing.builder()
                                .register(JsonSupport.create(JsonProcessing.create()))
                                .any((req, res) -> {
                                    logger.info("Got Request -" + count.incrementAndGet());
                                    req.next();
                                })
                                .get("/foo",(req, res) -> {
                                    res.status(Http.Status.I_AM_A_TEAPOT);
                                    res.send("foo");
                                })
                                .get("/bar", (req, res) -> {
                                    res.status(Http.Status.NOT_FOUND_404);
                                    res.send("No Bar!");
                                })
                                .any((req, res) -> {
                                    res.status(Http.Status.OK_200);
                                    res.send();
                                })
                                .build()
                );

        server.start()
                .whenComplete((webServer, throwable) ->
                        System.out.println("http://localhost:"+ webServer.port()+ "/bar"));


    }
}
