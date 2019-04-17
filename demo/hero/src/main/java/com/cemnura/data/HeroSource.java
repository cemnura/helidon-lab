package com.cemnura.data;

import io.opentracing.Span;
import io.opentracing.SpanContext;

import javax.json.*;
import javax.json.stream.JsonCollectors;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class HeroSource {

    private static JsonArray heroArray;

    static {

        try (InputStream is = new FileInputStream("heroes.json")){

            JsonReader reader = Json.createReader(is);

            heroArray = reader.readArray();

        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static JsonValue hero()
    {
        return heroArray.get(0);
    }

    public static JsonValue getVillains()
    {
        return heroArray
                .stream()
                .filter(jsonValue -> jsonValue.asJsonObject().getBoolean("villain"))
                .collect(JsonCollectors.toJsonArray());
    }

    public static JsonValue getHeroes()
    {
        return heroArray
                .stream()
                .filter(jsonValue -> !jsonValue.asJsonObject().getBoolean("villain"))
                .collect(JsonCollectors.toJsonArray());
    }

}
