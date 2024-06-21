package com.github.victorhsr.structuredconcurrency.context.orchestration;

import com.github.victorhsr.structuredconcurrency.context.PageContext;
import com.github.victorhsr.structuredconcurrency.context.details.ProductDetails;
import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;
import com.github.victorhsr.structuredconcurrency.context.recommendations.Recommendations;
import com.github.victorhsr.structuredconcurrency.context.reviews.ReviewsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.StructuredTaskScope;

public class PageContextScope extends StructuredTaskScope<PageContextComponent> {

    private volatile HeroData heroData;
    private volatile ReviewsData reviewsData;
    private volatile ProductDetails productDetails;
    private volatile Recommendations recommendations;
    private final List<Throwable> exceptions = new CopyOnWriteArrayList<>();

    @Override
    protected void handleComplete(Subtask<? extends PageContextComponent> subtask) {
        switch (subtask.state()) {
            case Subtask.State.SUCCESS -> onSuccess(subtask);
            case Subtask.State.FAILED -> exceptions.add(subtask.exception());
        }

        super.handleComplete(subtask);
    }

    private void onSuccess(Subtask<? extends PageContextComponent> subtask) {
        switch (subtask.get()) {
            case ProductDetails productDetailsFromSubtask -> this.productDetails = productDetailsFromSubtask;
            case HeroData heroDataFromSubTask -> this.heroData = heroDataFromSubTask;
            case ReviewsData reviewsDataFromSubTask -> this.reviewsData = reviewsDataFromSubTask;
            case Recommendations recommendationsFromTask -> this.recommendations = recommendationsFromTask;
            default -> throw new IllegalStateException(STR."Invalid output for subtask \{subtask}");
        }
    }

    public PageContext getContext() {
        super.ensureOwnerAndJoined();
        validateScope();
        return new PageContext(reviewsData, productDetails, heroData, recommendations);
    }

    private void validateScope() {
        List<String> missingComponents = new ArrayList<>();

        Objects.requireNonNullElseGet(heroData, () -> missingComponents.add("heroData"));
        Objects.requireNonNullElseGet(reviewsData, () -> missingComponents.add("reviewsData"));
        Objects.requireNonNullElseGet(productDetails, () -> missingComponents.add("productDetails"));
        Objects.requireNonNullElseGet(recommendations, () -> missingComponents.add("recommendations"));

        if (!missingComponents.isEmpty()) {
            PageContextException pageContextException = new PageContextException(STR."""
                    Failed to build ProductDetailsContextScope, missing components: \{missingComponents}
                    """);
            exceptions.forEach(pageContextException::addSuppressed);
            throw pageContextException;
        }
    }
}
