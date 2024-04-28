package com.github.victorhsr.structuredconcurrency;

import com.github.victorhsr.structuredconcurrency.context.ProductDetailsContext;
import com.github.victorhsr.structuredconcurrency.infrastructure.HeroClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ProductDetailsClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ReviewsClient;
import com.github.victorhsr.structuredconcurrency.context.orchestration.ProductDetailsContextScope;
import com.github.victorhsr.structuredconcurrency.context.orchestration.ProductDetailsException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductDetailsService {

    private final HeroClient heroClient;
    private final ProductDetailsClient productDetailsClient;
    private final ReviewsClient reviewsClient;

    public ProductDetailsContext getProductDetails(String productId) {

        try (var scope = new ProductDetailsContextScope()) {
            System.out.println("Scope started...");

            scope.fork(() -> heroClient.getHeroData(productId));
            scope.fork(() -> productDetailsClient.getDetails(productId));
            scope.fork(() -> reviewsClient.getReviews(productId));
            scope.join();

            return scope.getContext();
        } catch (ProductDetailsException | InterruptedException ex) {
            System.out.println("Failed to build context ");
            ex.printStackTrace();
            return null;
        }
    }
}
