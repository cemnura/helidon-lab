package com.cemnura.lab;

import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.StaticContentSupport;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.util.Collections;

public class Code9_Solution {

    private static final Logger logger = LogManager.getLogger(Code6_Solution.class);
    private static final JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Collections.emptyMap());

    public static void main(String[] args) {

        WebServer server =
                WebServer.create(
                        Routing.builder()
                                .any((req, res) -> { logger.info("Got any request"); req.next();})
                                .any("/path", (req, res) -> { logger.info("Got any request from /path"); req.next();})
                                .get("/path", (req, res) -> { res.send("You got Mail!");})
                                .get("/path1/{name}",(req, res) -> { /* handler */ res.send("Hi my name is " + req.path().param("name"));})
                                .get("/path2", (req, res) -> { logger.info("Got get request from /path2. Lets re route the request");  req.next(); })
                                .get("/path2", (req, res) -> { res.send("Just send a response!"); })
                                .post("/path", (req, res) -> { logger.info("Got post request from /path"); res.send("Are you not entertained?!"); })
                                .register(JsonSupport.create())
                                .post("/path2", (req, res) -> {
                                    logger.info("Lets post some JSON");
                                    JsonObject jsonObject = jsonFactory.createObjectBuilder().add("message", "No, I am your father").build();
                                    res.send(jsonObject);
                                })
                                .register("/path/content", StaticContentSupport.create("/images"))
                                .error(Exception.class, (req, res, ex) -> { logger.info(ex.getMessage());  res.send("Woops");})
                                .build()
                );

        server.start()
                .whenComplete((webServer, throwable) -> {
                    System.out.println("http://localhost:" + webServer.port());
                });


    }
}
