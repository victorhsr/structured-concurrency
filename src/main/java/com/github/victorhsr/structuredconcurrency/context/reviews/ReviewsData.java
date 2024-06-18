package com.github.victorhsr.structuredconcurrency.context.reviews;

import com.github.victorhsr.structuredconcurrency.context.orchestration.PageContextComponent;

import java.util.List;

public record ReviewsData(Double averageReviews, List<Review> reviews) implements PageContextComponent {
}