package com.github.victorhsr.structuredconcurrency.service;

import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextException;
import com.github.victorhsr.structuredconcurrency.context.recommendations.Recommendations;
import com.github.victorhsr.structuredconcurrency.infrastructure.recommendations.RecommendationsClientA;
import com.github.victorhsr.structuredconcurrency.infrastructure.recommendations.RecommendationsClientB;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

@RequiredArgsConstructor
public class RecommendationsService {

    private final RecommendationsClientA recommendationsClientA;
    private final RecommendationsClientB recommendationsClientB;

    public Recommendations getRecommendations(String productId) {

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Recommendations>()) {
            System.out.println("Recommendations SCOPE started...");

            scope.fork(() -> recommendationsClientA.getRecommendations(productId));
            scope.fork(() -> recommendationsClientB.getRecommendations(productId));
            scope.join();

            return scope.result();
        } catch (PageContextException | InterruptedException | ExecutionException ex) {
            System.out.println("Failed to get recommendations");
            ex.printStackTrace();
            return null;
        }
    }
}
