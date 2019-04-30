package com.cemnura.util;

import com.cemnura.entity.MovieCharacter;
import com.cemnura.entity.Quote;

import javax.json.*;
import javax.json.stream.JsonCollectors;
import java.util.Collections;
import java.util.List;

public class Converter {

    private static final JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Collections.emptyMap());

    public static MovieCharacter jsonToEntity(JsonObject jsonObject)
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

        quotes.forEach(movieCharacter::addQuote);

        return movieCharacter;
    }

    public static JsonObject entityToJson(MovieCharacter movieCharacter)
    {
        JsonObjectBuilder result = jsonFactory.createObjectBuilder().add("name", movieCharacter.getName());

        JsonArray quoteArray =  movieCharacter.getQuotes().stream()
                .map(quote -> jsonFactory.createObjectBuilder().add("quote", quote.getQuote()).build())
                .collect(JsonCollectors.toJsonArray());


        result.add("quotes", quoteArray);

        return result.build();
    }
}
