package com.cemnura.client;

import io.helidon.config.Config;
import io.helidon.tracing.jersey.client.ClientTracingFilter;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.glassfish.jersey.client.ClientRequest;


import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class QuoteServiceClient {

    private final Tracer tracer = GlobalTracer.get();

    Client client = ClientBuilder.newBuilder()
            .register(ClientTracingFilter.class)
            .register(ClientRequest.class)
            .build();

    private final String serviceEndpoint;

    public QuoteServiceClient(Config config) {
        this.serviceEndpoint = config.get("services.quote.endpoint").asString().get();
    }

    public JsonObject getQuote(String hero, SpanContext spanContext)
    {
        Span span = tracer.buildSpan("service.quote.call")
                .asChildOf(spanContext)
                .start();

        try {
            Response response = client.target(serviceEndpoint + "/quote/" + hero)
                    .request()
                    .property(ClientTracingFilter.TRACER_PROPERTY_NAME, tracer)
                    .property(ClientTracingFilter.CURRENT_SPAN_CONTEXT_PROPERTY_NAME, spanContext)
                    .get();

            return response.readEntity(JsonObject.class);
        }finally {
            span.finish();
        }

    }

}
