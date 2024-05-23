package com.github.victorhsr.structuredconcurrency.context.orchestration;

import com.github.victorhsr.structuredconcurrency.context.ProductDetailsContext;
import com.github.victorhsr.structuredconcurrency.context.details.ProductDetails;
import com.github.victorhsr.structuredconcurrency.context.hero.HeroData;
import com.github.victorhsr.structuredconcurrency.context.reviews.ReviewsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.StructuredTaskScope;

public class ProductDetailsContextScope extends StructuredTaskScope<ProductDetailsContextComponent> {

    private volatile HeroData heroData;
    private volatile ReviewsData reviewsData;
    private volatile ProductDetails productDetails;
    private final List<Throwable> exceptions = new CopyOnWriteArrayList<>();

    @Override
    protected void handleComplete(Subtask<? extends ProductDetailsContextComponent> subtask) {
        switch (subtask.state()) {
            case Subtask.State.SUCCESS -> onSuccess(subtask);
            case Subtask.State.FAILED -> exceptions.add(subtask.exception());
        }

        super.handleComplete(subtask);
    }

    private void onSuccess(Subtask<? extends ProductDetailsContextComponent> subtask) {
        switch (subtask.get()) {
            case ProductDetails productDetailsFromSubtask -> this.productDetails = productDetailsFromSubtask;
            case HeroData heroDataFromSubTask -> this.heroData = heroDataFromSubTask;
            case ReviewsData reviewsDataFromSubTask -> this.reviewsData = reviewsDataFromSubTask;
            default -> throw new IllegalStateException(STR."Invalid output for subtask \{subtask}");
        }
    }

    public ProductDetailsContext getContext() {
        super.ensureOwnerAndJoined();
        validateScope();
        return new ProductDetailsContext(reviewsData, productDetails, heroData);
    }

    private void validateScope() {
        List<String> missingComponents = new ArrayList<>();

        Objects.requireNonNullElseGet(heroData, () -> missingComponents.add("heroData"));
        Objects.requireNonNullElseGet(reviewsData, () -> missingComponents.add("reviewsData"));
        Objects.requireNonNullElseGet(productDetails, () -> missingComponents.add("productDetails"));

        if (!missingComponents.isEmpty()) {
            ProductDetailsException productDetailsException = new ProductDetailsException(STR."""
                    Failed to build ProductDetailsContextScope, missing components: \{missingComponents}
                    """);
            exceptions.forEach(productDetailsException::addSuppressed);
            throw productDetailsException;
        }
    }
}
