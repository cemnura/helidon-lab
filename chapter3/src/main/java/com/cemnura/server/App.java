package com.cemnura.server;


import com.cemnura.service.FibonacciService;
import io.helidon.microprofile.server.Server;

public class App {

    public static void main(String[] args) {
        Server server
                = Server.builder()
                .addResourceClass(FibonacciService.class)   // <1>
                .build();

        server.start(); // <2>
    }
}
