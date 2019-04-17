package com.cemnura.service;

import com.cemnura.dal.QuoteDBAccess;
import com.cemnura.entity.MovieCharacter;
import com.cemnura.entity.Quote;
import com.cemnura.exception.CharacterNotFoundException;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.webserver.*;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

import javax.json.*;
import javax.json.stream.JsonCollectors;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class QuoteHandler implements Service {

    private static final JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Collections.emptyMap());

    private final Tracer tracer = GlobalTracer.get();

    private final Gauge in_progress_request_gauge;
    private final Counter quote_provided_counter;

    public QuoteHandler() {
        quote_provided_counter = Counter.build("quote_provided_counter", "Counter for quote provided").register();
        in_progress_request_gauge = Gauge.build("quote_service_in_progress_request_gauge", "Gauge for request in progress").register();
    }

    @Override
    public void update(Routing.Rules rules) {

        rules
                .register(JsonSupport.create())
                .get("/{name}",this::getCharacterQuotes)
                .post("/register", Handler.create(JsonObject.class, this::register))
                ;

    }

    private void register(ServerRequest req, ServerResponse res, JsonObject jsonObject)
    {
        String name = jsonObject.getString("name");

        List<Quote> quotes = jsonObject.getJsonArray("quotes").getValuesAs(jsonValue -> {
            String quote = jsonValue.asJsonObject().getString("quote");
            Quote q = new Quote();

            q.setQuote(quote);
            return q;
        });

        MovieCharacter movieCharacter = new MovieCharacter();

        movieCharacter.setName(name);

        quotes.forEach(quote -> movieCharacter.addQuote(quote));


        QuoteDBAccess.insertCharacter(movieCharacter);

        res.send("Registered");
    }

    private void getCharacterQuotes(ServerRequest req, ServerResponse res)
    {
        String name = req.path().param("name");

        MovieCharacter movieCharacter = getMovieCharacter(name, req.spanContext());
        JsonObject result = prepareResponse(movieCharacter);

        res.send(result);
    }

    private MovieCharacter getMovieCharacter(String name, SpanContext spanContext)
    {

        Span span = tracer.buildSpan("quote.getMovieCharacter")
                .asChildOf(spanContext)
                .withTag("operation", "database.connect")
                .start();

        try {

            Optional<MovieCharacter> character = QuoteDBAccess.getCharacterByName(name);

            if (!character.isPresent()) {
                throw new CharacterNotFoundException("Character is not found");
            }

            MovieCharacter movieCharacter = character.get();

            return movieCharacter;

        }finally {
            span.finish();
        }
    }

    private JsonObject prepareResponse(MovieCharacter movieCharacter)
    {

        JsonObjectBuilder result = jsonFactory.createObjectBuilder().add("name", movieCharacter.getName());

        JsonArray quoteArray =  movieCharacter.getQuotes().stream()
                .map(quote -> jsonFactory.createObjectBuilder().add("quote", quote.getQuote()).build())
                .collect(JsonCollectors.toJsonArray());

        incQuoteProvidedCounter(quoteArray.size());

        result.add("quotes", quoteArray);

        return result.build();
    }

    private void incQuoteProvidedCounter(int size)
    {
        quote_provided_counter.inc(size);
    }
}


