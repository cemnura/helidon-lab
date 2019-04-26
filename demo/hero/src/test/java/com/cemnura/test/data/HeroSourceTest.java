package com.cemnura.test.data;


import com.cemnura.data.HeroSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

public class HeroSourceTest {

    public static final HeroSource heroSource = new HeroSource("src/test/resources/heroes.json");


    @Test
    public void when_getvillians_then_onlytrue()
    {
        heroSource.getVillains().asJsonArray()
                .stream()
                .map(
                        jsonValue -> {
                            return jsonValue.asJsonObject().getBoolean("villain");
                        }
                ).forEach(aBoolean -> {
                    Assert.assertTrue(aBoolean);
        });

    }

    @Test
    public void when_getheroes_then_onlyfalse()
    {
        heroSource.getHeroes().asJsonArray()
                .stream()
                .map(
                        jsonValue -> {
                            return jsonValue.asJsonObject().getBoolean("villain");
                        }
                ).forEach(aBoolean -> {
            Assert.assertFalse(aBoolean);
        });

    }
}
