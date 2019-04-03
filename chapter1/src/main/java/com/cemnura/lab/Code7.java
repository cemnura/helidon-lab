package com.cemnura.lab;

import com.cemnura.helper.SpoonException;
import io.helidon.common.http.Http;
import io.helidon.media.jsonp.common.JsonProcessing;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Code7 {

    private static final Logger logger = LogManager.getLogger(Code6_Solution.class);


    public static void main(String[] args) {


        WebServer server =
                WebServer.create(
                        Routing.builder()
                                .register(JsonSupport.create(JsonProcessing.create()))
                                .get("/foo",(req, res) -> {
                                    throw new SpoonException();
                                })
                                .get("/bar", (req, res) -> {
                                    throw new IllegalArgumentException();
                                })
                                .get("/zar", (req, res) -> {
                                    throw new IllegalStateException();
                                })
                                .error(SpoonException.class, (req, res, ex) -> {
                                    res.status(Http.Status.NOT_FOUND_404);
                                    res.send(ex.getMessage());
                                })
                                .error(IllegalArgumentException.class, (req, res, ex) -> {
                                    res.status(Http.Status.BAD_REQUEST_400);
                                    res.send("Wrong argument!");
                                })
                                .build()
                );

        server.start()
                .whenComplete((webServer, throwable) ->
                        System.out.println("http://localhost:"+ webServer.port()+ "/bar"));


    }
}
