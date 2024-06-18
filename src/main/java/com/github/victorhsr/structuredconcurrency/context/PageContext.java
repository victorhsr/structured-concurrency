package com.github.victorhsr.structuredconcurrency.context;

import com.github.victorhsr.structuredconcurrency.context.details.Page;
import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;
import com.github.victorhsr.structuredconcurrency.context.recommendations.Recommendations;
import com.github.victorhsr.structuredconcurrency.context.reviews.ReviewsData;

public record PageContext(ReviewsData reviewsData, Page productDetails, HeroData heroData, Recommendations recommendations) {

}
