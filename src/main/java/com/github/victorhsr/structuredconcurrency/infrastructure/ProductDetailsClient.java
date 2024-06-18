package com.github.victorhsr.structuredconcurrency.infrastructure;

import com.github.victorhsr.structuredconcurrency.context.details.Page;

import java.util.Map;

public class ProductDetailsClient {

    private static final Map<String, Page> DATA_MAP;

    static {
        DATA_MAP = Map.of(
                "1", new Page(Map.of("Storage", "500GB")),
                "2", new Page(Map.of("Storage", "700GB")),
                "4", new Page(Map.of("Storage", "1TB"))
        );
    }

    public Page getDetails(String productId) throws InterruptedException {
        System.out.println("ProductDetailsClient getting results...");
        Thread.sleep(200);
        if (!DATA_MAP.containsKey(productId)) {
            throw new IllegalArgumentException(STR."productId not found \{productId}");
        }
        return DATA_MAP.get(productId);
    }

}
