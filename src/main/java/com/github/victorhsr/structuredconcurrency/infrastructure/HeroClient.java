package com.github.victorhsr.structuredconcurrency.infrastructure;

import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;

import java.util.Map;

public class HeroClient {

    private static final Map<String, HeroData> DATA_MAP;

    static {
        DATA_MAP = Map.of(
                "1", new HeroData("Playstation 5", "https://www.my-image-host/1"),
                "3", new HeroData("Playstation 4", "https://www.my-image-host/2"),
                "4", new HeroData("Xbox One", "https://www.my-image-host/4")
        );
    }

    public HeroData getHeroData(String productId) throws InterruptedException {
        System.out.println("HeroClient getting results...");
        Thread.sleep(150);
        if (!DATA_MAP.containsKey(productId)) {
            throw new IllegalArgumentException(STR."productId not found \{productId}");
        }
        return DATA_MAP.get(productId);
    }

}
