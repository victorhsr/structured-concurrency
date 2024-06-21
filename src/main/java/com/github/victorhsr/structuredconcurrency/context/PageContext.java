package com.github.victorhsr.structuredconcurrency.context;

import com.github.victorhsr.structuredconcurrency.context.details.ProductDetails;
import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;
import com.github.victorhsr.structuredconcurrency.context.recommendations.Recommendations;
import com.github.victorhsr.structuredconcurrency.context.reviews.ReviewsData;

public record PageContext(ReviewsData reviewsData, ProductDetails productDetails, HeroData heroData, Recommendations recommendations) {

}
