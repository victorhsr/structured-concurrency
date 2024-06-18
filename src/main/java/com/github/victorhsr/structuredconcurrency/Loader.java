package com.github.victorhsr.structuredconcurrency;

import com.github.victorhsr.structuredconcurrency.infrastructure.HeroClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ProductDetailsClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ReviewsClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.recommendations.RecommendationsClientA;
import com.github.victorhsr.structuredconcurrency.infrastructure.recommendations.RecommendationsClientB;
import com.github.victorhsr.structuredconcurrency.service.PageContextService;
import com.github.victorhsr.structuredconcurrency.service.RecommendationsService;

public class Loader {

    public static void main(String[] args) {
        PageContextService pageContextService = new PageContextService(
                new HeroClient(),
                new ProductDetailsClient(),
                new ReviewsClient(),
                new RecommendationsService(new RecommendationsClientA(), new RecommendationsClientB())
        );

        System.out.println(STR."Built context = \{pageContextService.getPageContext("1")}");
        System.out.println("-----");
        System.out.println(STR."Built context = \{pageContextService.getPageContext("2")}");
        System.out.println("-----");
        System.out.println(STR."Built context = \{pageContextService.getPageContext("3")}");
        System.out.println("-----");
        System.out.println(STR."Built context = \{pageContextService.getPageContext("4")}");
    }

}
