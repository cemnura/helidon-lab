package com.cemnura.service;

import com.cemnura.data.HeroSource;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.*;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import io.prometheus.client.*;
import io.prometheus.client.Counter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.JsonValue;
import java.util.function.Function;
import java.util.function.Predicate;

public class HeroHandler implements Service{

    private static final Logger logger = LogManager.getLogger(HeroHandler.class);

    private static final HeroSource heroSource = new HeroSource("heroes.json");

    private final Tracer tracer = GlobalTracer.get();

    private final Gauge in_progress_request_gauge;
    private final Counter hero_request_counter;
    private final Counter villain_request_counter;


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
                  .get("/heroes", RequestPredicate.create().containsQueryParameter("startsWith").thenApply(this::getHeroesStartsWith))
                  .get("/heroes", this::getHeroes)
                  .get("/villains", this::incVillainRequestCount)
                  .get("/villains", this::getVillains)
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

        int index = Integer.parseInt(id);

        Span span = tracer.buildSpan("HeroSource.getHeroById").asChildOf(req.spanContext()).start();

        try {
            JsonValue jsonValue = heroSource.getByIndex(index);

            sendResponse(res, jsonValue);

        }finally {

            span.finish();
        }

    }

    private void getAll(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getAll").asChildOf(req.spanContext()).start();

        JsonValue responseContent = heroSource.getAll();

        sendResponse(res, responseContent);

        span.finish();
    }

    private void getHeroes(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getHeroes").asChildOf(req.spanContext()).start();

        JsonValue responseContent = heroSource.getHeroes();

        sendResponse(res, responseContent);

        span.finish();
    }

    private void getHeroesStartsWith(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getHeroesStartsWith").asChildOf(req.spanContext()).start();

        String filter = req.queryParams().first("startsWith").orElseThrow(IllegalArgumentException::new);

        Function<String, Predicate<JsonValue>> filterFunction = s -> json -> json.asJsonObject().getString("name").startsWith(s);

        JsonValue responseContent = heroSource.getHeroes(filterFunction.apply(filter));

        sendResponse(res, responseContent);

        span.finish();
    }

    private void getVillains(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getVillains").asChildOf(req.spanContext()).start();

        JsonValue responseContent = heroSource.getVillains();

        sendResponse(res, responseContent);

        span.finish();
    }

    private void getRandomHero(ServerRequest req, ServerResponse res)
    {
        Span span = tracer.buildSpan("HeroSource.getRandom").asChildOf(req.spanContext()).start();

        JsonValue result = heroSource.getRandom();

        sendResponse(res, result);

        span.finish();
    }

}