package com.cemnura.service;

import com.cemnura.client.QuoteServiceClient;
import com.cemnura.data.HeroSource;
import io.helidon.config.Config;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.*;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import io.prometheus.client.*;
import io.prometheus.client.Counter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.*;
import javax.json.stream.JsonGeneratorFactory;
import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class HeroHandler implements Service{

    private static final Logger logger = LogManager.getLogger(HeroHandler.class);

    private static final HeroSource heroSource = new HeroSource("heroes.json");

    private final Tracer tracer = GlobalTracer.get();

    private final Gauge in_progress_request_gauge;
    private final Counter hero_request_counter;
    private final Counter villain_request_counter;
    private static final JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Collections.emptyMap());

    private static final QuoteServiceClient quoteClient = new QuoteServiceClient(Config.create());


    public HeroHandler() {
        hero_request_counter = Counter.build("hero_request_counter", "Counter for request made to heroes").register();
        villain_request_counter = Counter.build("villain_request_counter", "Counter for request made to heroes").register();
        in_progress_request_gauge = Gauge.build("hero_service_in_progress_request_gauge", "Gauge for request in progress").register();
    }

    @Override
    public void update(Routing.Rules rules) {
          rules
                  .any(this::logRequest)
                  .any(this::incProgressGauge)
                  .register(JsonSupport.create())
                  .get("/id/{id}", this::getHeroById)
                  .get("/heroes", this::incHeroRequestCount)
                  .get("/heroes", RequestPredicate.create()
                          .containsQueryParameter("startsWith").thenApply(this::getHeroesStartsWith)
                          .otherwise(this::getHeroes))
                  .get("/villains", this::incVillainRequestCount)
                  .get("/villains", RequestPredicate.create()
                          .containsQueryParameter("startsWith").thenApply(this::getVillainsStartsWith)
                          .otherwise(this::getVillains))
                  .get("/all", this::getAll)
                  .get("/random", this::getRandomHero);
    }


    private void logRequest(ServerRequest req, ServerResponse res)
    {
        logger.info("Retrieved request");
        req.next();
    }

    private void incProgressGauge(ServerRequest req, ServerResponse res)
    {
        in_progress_request_gauge.inc();
        req.next();
    }

    private void decProcessGauge()
    {
        in_progress_request_gauge.dec();
    }

    private void incHeroRequestCount(ServerRequest req, ServerResponse res)
    {
        hero_request_counter.inc();
        req.next();
    }

    private void incVillainRequestCount(ServerRequest req, ServerResponse res)
    {
        villain_request_counter.inc();
        req.next();
    }

    private <T> void sendResponse(ServerResponse res, T content)
    {
        res.send(content);
        decProcessGauge();
    }

    private void getHeroById(ServerRequest req, ServerResponse res)
    {
        String id = req.path().param("id");
        Span span = tracer.buildSpan("HeroSource.getHeroById").asChildOf(req.spanContext()).start();

        try {

            int index = Integer.parseInt(id);

            JsonValue jsonValue = heroSource.getByIndex(index);

            if (req.queryParams().first("getQuotes").isPresent() && Boolean.valueOf(req.queryParams().first("getQuotes").get()))
                jsonValue = getQuoteForHero(req, jsonValue);

            sendResponse(res, jsonValue);

        }catch (Exception ex) {
            decProcessGauge();
            throw ex;
        }finally {
            span.finish();
        }
    }

    private void getAll(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getAll").asChildOf(req.spanContext()).start();

        try {
            JsonValue responseContent = heroSource.getAll();

            sendResponse(res, responseContent);
        }finally {
            span.finish();
        }
    }

    private void getHeroes(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getHeroes").asChildOf(req.spanContext()).start();

        try {
            JsonValue responseContent = heroSource.getHeroes();
            sendResponse(res, responseContent);
        }finally {
            span.finish();
        }

    }

    private void getHeroesStartsWith(ServerRequest req, ServerResponse res)
    {
        String startsWith = req.queryParams().first("startsWith").orElseThrow(IllegalArgumentException::new);

        Span span = tracer.buildSpan("HeroSource.getHeroesStartsWith").asChildOf(req.spanContext()).start();

        try {
            JsonValue responseContent = heroSource.getHeroes(startsWith);

            sendResponse(res, responseContent);
        }finally {
            span.finish();
        }

    }

    private void getVillains(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getVillains").asChildOf(req.spanContext()).start();

        try {
            JsonValue responseContent = heroSource.getVillains();

            sendResponse(res, responseContent);
        }finally {
            span.finish();
        }

    }

    private void getVillainsStartsWith(ServerRequest req, ServerResponse res)
    {
        String startsWith = req.queryParams().first("startsWith").orElseThrow(IllegalArgumentException::new);

        Span span = tracer.buildSpan("HeroSource.getVillainsStartsWith").asChildOf(req.spanContext()).start();

        try {
            JsonValue responseContent = heroSource.getVillains(startsWith);

            sendResponse(res, responseContent);
        }finally {
            span.finish();
        }

    }

    private void getRandomHero(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getRandom").asChildOf(req.spanContext()).start();

        try {
            JsonValue result = heroSource.getRandom();

            if (req.queryParams().first("getQuotes").isPresent() && Boolean.valueOf(req.queryParams().first("getQuotes").get()))
                result = getQuoteForHero(req, result);

            sendResponse(res, result);
        }finally {
            span.finish();
        }
    }

    private JsonObject getQuoteForHero(ServerRequest req, JsonValue jsonValue)
    {
        String name = jsonValue.asJsonObject().getString("name");

        JsonObject quotes = quoteClient.getQuote(name, req.spanContext());

        JsonObjectBuilder builder = jsonFactory.createObjectBuilder(jsonValue.asJsonObject()).add("quotes", quotes.getJsonArray("quotes"));
        return builder.build();
    }


}