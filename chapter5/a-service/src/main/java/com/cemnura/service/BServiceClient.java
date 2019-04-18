package com.cemnura.service;

import io.helidon.config.Config;
import io.helidon.tracing.jersey.client.ClientTracingFilter;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class BServiceClient {

    private final Tracer tracer = GlobalTracer.get();

    Client client = ClientBuilder.newBuilder()
            .register(ClientTracingFilter.class)
            .build();

    private final String serviceEndpoint;

    public BServiceClient(Config config) {
        this.serviceEndpoint = config.get("services.b.endpoint").asString().get();
    }

    public String callService(SpanContext spanContext)
    {
        Span span = tracer.buildSpan("service.b.call")
                .asChildOf(spanContext)
                .start();

        try {
            Response response = client.target(serviceEndpoint + "/")
                    .request()
                    .property(ClientTracingFilter.TRACER_PROPERTY_NAME, tracer)
                    .property(ClientTracingFilter.CURRENT_SPAN_CONTEXT_PROPERTY_NAME, spanContext)
                    .get();
            return response.readEntity(String.class);
        }finally {
            span.finish();
        }

    }
}
