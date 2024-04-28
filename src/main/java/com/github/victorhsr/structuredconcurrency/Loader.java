package com.github.victorhsr.structuredconcurrency;

import com.github.victorhsr.structuredconcurrency.infrastructure.HeroClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ProductDetailsClient;
import com.github.victorhsr.structuredconcurrency.infrastructure.ReviewsClient;

public class Loader {

    public static void main(String[] args) {
        ProductDetailsService productDetailsService = new ProductDetailsService(
                new HeroClient(),
                new ProductDetailsClient(),
                new ReviewsClient()
        );

        System.out.println(STR."Built context = \{productDetailsService.getProductDetails("1")}");
        System.out.println("-----");
        System.out.println(STR."Built context = \{productDetailsService.getProductDetails("2")}");
        System.out.println("-----");
        System.out.println(STR."Built context = \{productDetailsService.getProductDetails("3")}");
        System.out.println("-----");
        System.out.println(STR."Built context = \{productDetailsService.getProductDetails("4")}");
    }

}
