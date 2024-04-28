package com.github.victorhsr.structuredconcurrency.infrastructure;

import com.github.victorhsr.structuredconcurrency.context.details.ProductDetails;
import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;

import java.util.Map;

public class ProductDetailsClient {

    private static final Map<String, ProductDetails> DATA_MAP;

    static {
        DATA_MAP = Map.of(
                "1", new ProductDetails(Map.of("Storage", "500GB")),
                "2", new ProductDetails(Map.of("Storage", "700GB")),
                "4", new ProductDetails(Map.of("Storage", "1TB"))
        );
    }

    public ProductDetails getDetails(String productId) {
        if (!DATA_MAP.containsKey(productId)) {
            throw new IllegalArgumentException(STR."productId not found \{productId}");
        }
        System.out.println("ProductDetailsClient getting results...");
        return DATA_MAP.get(productId);
    }

}
