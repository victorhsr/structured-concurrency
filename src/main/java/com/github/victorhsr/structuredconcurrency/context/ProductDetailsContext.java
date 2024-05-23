package com.github.victorhsr.structuredconcurrency.context;

import com.github.victorhsr.structuredconcurrency.context.details.ProductDetails;
import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;
import com.github.victorhsr.structuredconcurrency.context.reviews.ReviewsData;

public record ProductDetailsContext(ReviewsData reviewsData, ProductDetails productDetails, HeroData heroData) {

}
