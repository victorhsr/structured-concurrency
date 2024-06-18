package com.github.victorhsr.structuredconcurrency.infrastructure.recommendations;

import com.github.victorhsr.structuredconcurrency.context.recommendations.RecommendationProduct;
import com.github.victorhsr.structuredconcurrency.context.recommendations.Recommendations;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;

public class RecommendationsClientB {

    private static final Map<String, Recommendations> DATA_MAP;

    static {
        DATA_MAP = Map.of(
                "1", new Recommendations(List.of(new RecommendationProduct("Playstation 5 refurbished", "https://www.my-image-host/recommendation/1", "$1.00"))),
                "2", new Recommendations(List.of(new RecommendationProduct("Playstation 4 refurbished", "https://www.my-image-host/recommendation/2", "$2.00"))),
                "3", new Recommendations(List.of(new RecommendationProduct("XBox 360 refurbished", "https://www.my-image-host/recommendation/3", "$3.00")))
        );
    }

    @SneakyThrows
    public Recommendations getRecommendations(String productId) {
        System.out.println("RecommendationsClientB getting results...");
        Thread.sleep(150);
        if (!DATA_MAP.containsKey(productId)) {
            throw new IllegalArgumentException(STR."productId not found \{productId}");
        }
        return DATA_MAP.get(productId);
    }

}
