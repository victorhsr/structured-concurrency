package com.github.victorhsr.structuredconcurrency.service;

import com.github.victorhsr.structuredconcurrency.context.PageContext;
import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextScope;
import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextException;
import com.github.victorhsr.structuredconcurrency.infrastructure.HeroClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ProductDetailsClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ReviewsClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PageContextService {

    private final HeroClient heroClient;
    private final ProductDetailsClient productDetailsClient;
    private final ReviewsClient reviewsClient;
    private final RecommendationsService recommendationsService;

    public PageContext getPageContext(String productId) {

        try (var scope = new PageContextScope()) {
            System.out.println("PageContext SCOPE started...");

            scope.fork(() -> heroClient.getHeroData(productId));
            scope.fork(() -> productDetailsClient.getDetails(productId));
            scope.fork(() -> reviewsClient.getReviews(productId));
            scope.fork(() -> recommendationsService.getRecommendations(productId));
            scope.join();

            return scope.getContext();
        } catch (PageContextException | InterruptedException ex) {
            System.out.println("Failed to build context ");
            ex.printStackTrace();
            return null;
        }
    }
}
