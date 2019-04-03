package com.cemnura.lab;

import io.helidon.common.http.MediaType;
import io.helidon.webserver.Routing;
import io.helidon.webserver.StaticContentSupport;
import io.helidon.webserver.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Code8 {

    private static final Logger logger = LogManager.getLogger(Code6_Solution.class);

    public static void main(String[] args) {

        WebServer server =
                WebServer.create(
                        Routing.builder()
                                .register("/image", StaticContentSupport.create("/images")) // <1>
                                .register("/", StaticContentSupport
                                        .builder("/pages") // <2>
                                        .welcomeFileName("index.html")  // <3>
                                        .contentType("html", MediaType.TEXT_HTML)) // <4>
                                .build()
                );

        server.start()
                .whenComplete((webServer, throwable) -> {
                        System.out.println("http://localhost:"+ webServer.port()+ "/image/image.jpg");
                        System.out.println("http://localhost:"+ webServer.port()+ "/");
                        System.out.println("http://localhost:"+ webServer.port()+ "/sample.html");
                });


    }
}
