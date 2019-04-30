package com.cemnura.service;

import com.cemnura.dal.QuoteDBAccess;
import com.cemnura.entity.MovieCharacter;
import com.cemnura.util.Converter;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.*;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;


import javax.json.*;
import java.util.Collections;


public class QuoteHandler implements Service {

    private final Tracer tracer = GlobalTracer.get();

    private final Gauge in_progress_request_gauge;
    private final Counter quote_provided_counter;

    public QuoteHandler() {
        quote_provided_counter = Counter.build("quote_provided_counter", "Counter for quote provided").register();
        in_progress_request_gauge = Gauge.build(        "quote_service_in_progress_request_gauge", "Gauge for request in progress").register();
    }

    @Override
    public void update(Routing.Rules rules) {

        rules
                .register(JsonSupport.create())
                .any(this::incProgressGauge)
                .get("/{name}",this::getCharacterQuotes)
                .post("/register", Handler.create(JsonObject.class, this::register))
                .post("/append", Handler.create(JsonObject.class, this::appendQuotes))
                ;

    }

    private void appendQuotes(ServerRequest req, ServerResponse res, JsonObject jsonObject)
    {
        MovieCharacter movieCharacter = Converter.jsonToEntity(jsonObject);

        Span span = tracer.buildSpan("quote.appendQuotes")
                .asChildOf(req.spanContext())
                .withTag("operation", "database.persist")
                .start();

        try {
            QuoteDBAccess.appendQuotes(movieCharacter.getName(), movieCharacter.getQuotes());
        }finally {
            span.finish();
        }

        res.send("Success");
        decProgressGauge();
    }

    private void register(ServerRequest req, ServerResponse res, JsonObject jsonObject)
    {
        MovieCharacter movieCharacter = Converter.jsonToEntity(jsonObject);

        Span span = tracer.buildSpan("quote.register")
                .asChildOf(req.spanContext())
                .withTag("operation", "database.persist")
                .start();

        try {
            QuoteDBAccess.insertCharacter(movieCharacter);
        }finally {
            span.finish();
        }

        res.send("Registered");
        decProgressGauge();
    }

    private void getCharacterQuotes(ServerRequest req, ServerResponse res)
    {
        String name = req.path().param("name");

        MovieCharacter movieCharacter = getMovieCharacter(name, req.spanContext());
        JsonObject result = prepareResponse(movieCharacter);

        res.send(result);
        decProgressGauge();
    }

    private MovieCharacter getMovieCharacter(String name, SpanContext spanContext)
    {
        Span span = tracer.buildSpan("quote.getMovieCharacter")
                .asChildOf(spanContext)
                .withTag("operation", "database.connect")
                .start();

        try {
            MovieCharacter character = QuoteDBAccess.getCharacterByName(name);

            return character;
        }finally {
            span.finish();
        }
    }

    private JsonObject prepareResponse(MovieCharacter movieCharacter)
    {
        JsonObject jsonObject = Converter.entityToJson(movieCharacter);

        incQuoteProvidedCounter(jsonObject.getJsonArray("quotes").size());

        return jsonObject;
    }

    private void incQuoteProvidedCounter(int size)
    {
        quote_provided_counter.inc(size);
    }
    private void incProgressGauge(ServerRequest req, ServerResponse res)
    {
        in_progress_request_gauge.inc();
        req.next();
    }
    private void decProgressGauge()
    {
        in_progress_request_gauge.dec();
    }
}


