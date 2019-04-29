package com.cemnura.data;

import io.opentracing.Span;
import io.opentracing.SpanContext;

import javax.json.*;
import javax.json.stream.JsonCollectors;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

public class HeroSource {

    private static JsonArray heroArray;

    private static final Function<String, Predicate<JsonValue>> filterFunction = startsWith -> json -> json.asJsonObject().getString("name").startsWith(startsWith);

    public HeroSource(String file) {
        try (InputStream is = new FileInputStream(file)){

            JsonReader reader = Json.createReader(is);

            heroArray = reader.readArray();

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JsonValue getByIndex(int index)
    {
        return heroArray.get(index);
    }

    public JsonValue getAll()
    {
        return heroArray;
    }

    public JsonValue getVillains()
    {
        return heroArray
                .stream()
                .filter(jsonValue -> jsonValue.asJsonObject().getBoolean("villain"))
                .collect(JsonCollectors.toJsonArray());
    }

    public JsonValue getVillains(String startWith)
    {
        return heroArray
                .stream()
                .filter(jsonValue -> jsonValue.asJsonObject().getBoolean("villain"))
                .filter(filterFunction.apply(startWith))
                .collect(JsonCollectors.toJsonArray());
    }

    public JsonValue getHeroes()
    {
        return heroArray
                .stream()
                .filter(jsonValue -> !jsonValue.asJsonObject().getBoolean("villain"))
                .collect(JsonCollectors.toJsonArray());
    }

    public JsonValue getHeroes(String startWith)
    {
        return heroArray
                .stream()
                .filter(jsonValue -> !jsonValue.asJsonObject().getBoolean("villain"))
                .filter(filterFunction.apply(startWith))
                .collect(JsonCollectors.toJsonArray());
    }

    public JsonValue getRandom()
    {
        Random random = new Random();
        int randomIndex = random.nextInt(heroArray.size());

        return heroArray.get(randomIndex);
    }

}
