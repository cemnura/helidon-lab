package com.cemnura.test.data;


import com.cemnura.data.HeroSource;
import org.junit.Assert;
import org.junit.Test;

public class HeroSourceTest {

    @Test
    public void when_getvillians_then_onlytrue()
    {
        HeroSource.getVillains().asJsonArray()
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
        HeroSource.getHeroes().asJsonArray()
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
